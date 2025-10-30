package ps.demo.jpademo.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private DeadLetterRepository deadLetterRepository;

    @Autowired
    private TrackingRepository trackingRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Transactional
    public MessageResponse sendMessage(MessageRequest request) {
        MessageEntity entity = MessageConverter.toEntity(request, "system");
        entity.setMaxRetryCount(3); // Default max retry count
        messageRepository.save(entity);
        trackMessage(entity.getId(), null, "PENDING", "Message created");
        processMessageAsync(entity);
        return MessageConverter.toResponse(entity);
    }

    @Async
    public CompletableFuture<Void> processMessageAsync(MessageEntity entity) {
        try {
            List<SubscriberEntity> subscribers = getSubscribersForMessage(entity);
            if (entity.getOrdered()) {
                subscribers.forEach(subscriber -> pushMessageToSubscriber(entity, subscriber));
            } else {
                subscribers.parallelStream().forEach(subscriber -> pushMessageToSubscriber(entity, subscriber));
            }
            entity.setStatus("SENT");
            messageRepository.save(entity);
            trackMessage(entity.getId(), null, "SENT", "Message sent to all subscribers");
        } catch (Exception e) {
            handleFailure(entity, null, e);
        }
        return CompletableFuture.completedFuture(null);
    }

    private List<SubscriberEntity> getSubscribersForMessage(MessageEntity entity) {
        return subscriptionRepository.findByMessageType(entity.getContentType())
                .stream()
                .map(SubscriptionEntity::getSubscriber)
                .collect(Collectors.toList());
    }

    private void pushMessageToSubscriber(MessageEntity entity, SubscriberEntity subscriber) {
        try {
            // Simulate pushing message to subscriber endpoint
            Thread.sleep(100);
            trackMessage(entity.getId(), subscriber.getId(), "DELIVERED", "Message delivered to subscriber: " + subscriber.getName());
        } catch (Exception e) {
            trackMessage(entity.getId(), subscriber.getId(), "FAILED", "Failed to deliver to subscriber: " + subscriber.getName() + ", Reason: " + e.getMessage());
            if (entity.getRetryCount() < entity.getMaxRetryCount()) {
                entity.setRetryCount(entity.getRetryCount() + 1);
                messageRepository.save(entity);
                pushMessageToSubscriber(entity, subscriber);
            } else {
                handleFailure(entity, subscriber, e);
            }
        }
    }

    private void handleFailure(MessageEntity entity, SubscriberEntity subscriber, Exception e) {
        entity.setStatus("FAILED");
        messageRepository.save(entity);
        trackMessage(entity.getId(), subscriber != null ? subscriber.getId() : null, "FAILED", "Message failed after max retries: " + e.getMessage());
        DeadLetterEntity deadLetter = new DeadLetterEntity();
        deadLetter.setMessageId(entity.getId());
        if (subscriber != null) {
            deadLetter.setSubscriberId(subscriber.getId());
            deadLetter.setSubscriberName(subscriber.getName());
            deadLetter.setSubscriberEndpoint(subscriber.getEndpoint());
        }
        deadLetter.setReason(e.getMessage());
        deadLetter.setStackTrace(e.toString());
        deadLetter.setCreatedBy("system");
        deadLetterRepository.save(deadLetter);
    }

    private void trackMessage(Long messageId, Long subscriberId, String status, String details) {
        TrackingEntity tracking = new TrackingEntity();
        tracking.setMessageId(messageId);
        tracking.setSubscriberId(subscriberId);
        tracking.setStatus(status);
        tracking.setTimestamp(LocalDateTime.now());
        tracking.setDetails(details);
        if (subscriberId != null) {
            SubscriberEntity subscriber = subscriberRepository.findById(subscriberId).orElse(null);
            if (subscriber != null) {
                tracking.setSubscriberName(subscriber.getName());
                tracking.setSubscriberEndpoint(subscriber.getEndpoint());
            }
        }
        trackingRepository.save(tracking);
    }
}