
CREATE TABLE tbl_user (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50),
  email VARCHAR(50),
  password VARCHAR(100),
  role VARCHAR(20)
);

CREATE TABLE product (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100),
  description TEXT,
  price DECIMAL(10,2),
  image_url VARCHAR(255)
);

CREATE TABLE stock (
  id INT PRIMARY KEY AUTO_INCREMENT,
  product_id INT NOT NULL,
  quantity INT DEFAULT 0,
  FOREIGN KEY (product_id)
    REFERENCES product(id)
);

CREATE TABLE cart (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT UNIQUE,
  total_price DECIMAL(10,2),
  created_at TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES tbl_user(id)
);

CREATE TABLE cart_item (
  id INT PRIMARY KEY AUTO_INCREMENT,
  cart_id INT,
  product_id INT,
  quantity INT,
  total_price DECIMAL(10,2),
  FOREIGN KEY (cart_id) REFERENCES cart(id),
  FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE tbl_order (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT,
  total_price DECIMAL(10,2),
  payment_method VARCHAR(50),
  transaction_id VARCHAR(50),
  created_at TIMESTAMP,
  status VARCHAR(20),
  FOREIGN KEY (user_id) REFERENCES tbl_user(id)
);

CREATE TABLE order_item (
  id INT PRIMARY KEY AUTO_INCREMENT,
  order_id INT,
  product_id INT,
  quantity INT,
  total_price DECIMAL(10,2),
  FOREIGN KEY (order_id) REFERENCES tbl_order(id),
  FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE payment (
  id INT PRIMARY KEY AUTO_INCREMENT,
  order_id INT,
  payment_method VARCHAR(50),
  card_no VARCHAR(50),
  expiry_date TIMESTAMP,
  cvc_no VARCHAR(50),
  transaction_id VARCHAR(50),
  amount DECIMAL(10,2),
  created_at TIMESTAMP,
  FOREIGN KEY (order_id) REFERENCES tbl_order(id)
);


CREATE TABLE my_mock (
  id INT PRIMARY KEY AUTO_INCREMENT,
  uri VARCHAR(50),
  regex_match BOOLEAN,
  method VARCHAR(50),
  status INT,
  headers TEXT,
  body TEXT,
  created_at TIMESTAMP
);