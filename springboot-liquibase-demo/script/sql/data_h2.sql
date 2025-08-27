

INSERT INTO tbl_user (name, email, password, role)
VALUES
  ('John Doe','john@example.com','','user'),
  ('Jane Doe','jane@example.com','','user'),
  ('Bob Smith','bob@example.com','','admin');


INSERT INTO product (name, description, price, image_url)
VALUES
  ('Product 1','Description for product 1',19.99,'https://image.jpg'),
  ('Product 2','Description for product 2',29.99,'https://image2.jpg'),
  ('Product 3','Description for product 3',39.99,'https://image3.jpg'),
  ('Product 4', 'Description for product 4', 49.99, 'https://image4.jpg'),
  ('Product 5', 'Description for product 5', 14.99, 'https://image5.jpg'),
  ('Product 6', 'Description for product 6', 24.99, 'https://image6.jpg'),
  ('Product 7', 'Description for product 7', 9.99, 'https://image7.jpg'),
  ('Product 8', 'Description for product 8', 19.99, 'https://image8.jpg'),
  ('Product 9', 'Description for product 9', 29.99, 'https://image9.jpg'),
  ('Product 10', 'Description for product 10', 39.99, 'https://image10.jpg'),
  ('Product 11', 'Description for product 11', 59.99, 'https://image11.jpg'),
  ('Product 12', 'Description for product 12', 69.99, 'https://image12.jpg'),
  ('Product 13', 'Description for product 13', 79.99, 'https://image13.jpg'),
  ('Product 14', 'Description for product 14', 89.99, 'https://image14.jpg'),
  ('Product 15', 'Description for product 15', 99.99, 'https://image15.jpg'),
  ('Product 16', 'Description for product 16', 59.99, 'https://image16.jpg'),
  ('Product 17', 'Description for product 17', 79.99, 'https://image17.jpg');

INSERT INTO stock (product_id, quantity)
VALUES
  (1, 10),
  (2, 15),
  (3, 15),
  (4, 15),
  (5, 15),
  (6, 15),
  (7, 15),
  (8, 15),
  (9, 15),
  (10, 15),
  (11, 15),
  (12, 15),
  (13, 15),
  (14, 15),
  (15, 15),
  (16, 15),
  (17, 20);


INSERT INTO cart (user_id, total_price, created_at)
VALUES (1, 69.97, CURRENT_TIMESTAMP);


INSERT INTO cart_item (cart_id, product_id, quantity, total_price)
VALUES
  (1, 1, 2, 39.98),
  (1, 2, 1, 29.99);


INSERT INTO tbl_order (user_id, total_price, payment_method, transaction_id, created_at, status)
VALUES
  (1, 69.97, 'credit_card', 'tx123', CURRENT_TIMESTAMP, 'completed'),
  (2, 19.99, 'paypal', 'tx456', CURRENT_TIMESTAMP, 'pending');


INSERT INTO order_item (order_id, product_id, quantity, total_price)
VALUES
  (1, 1, 2, 39.98),
  (1, 2, 1, 29.99),
  (2, 1, 1, 19.99);


INSERT INTO payment (order_id, payment_method, card_no, expiry_date, cvc_no, transaction_id, amount, created_at)
VALUES
  (1, 'credit_card', '4111111111111111', '2025-02-28', '123', 'tx123', 69.97, CURRENT_TIMESTAMP);