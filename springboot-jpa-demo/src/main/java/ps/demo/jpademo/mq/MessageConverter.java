package ps.demo.jpademo.mq;

public class MessageConverter {

    public static MessageEntity toEntity(MessageRequest request, String createdBy) {
        MessageEntity entity = new MessageEntity();
        entity.setContent(request.getContent());
        entity.setRecipient(request.getRecipient());
        entity.setStatus("PENDING");
        entity.setRetryCount(0);
        entity.setOrdered(request.getOrdered());
        entity.setCreatedBy(createdBy);
        entity.setUpdatedBy(createdBy);
        return entity;
    }

    public static MessageResponse toResponse(MessageEntity entity) {
        MessageResponse response = new MessageResponse();
        response.setId(entity.getId());
        response.setContent(entity.getContent());
        response.setRecipient(entity.getRecipient());
        response.setStatus(entity.getStatus());
        response.setOrdered(entity.getOrdered());
        return response;
    }
}