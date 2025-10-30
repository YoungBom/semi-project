-- Active: 1758675368804@@127.0.0.1@3306@semi_project
CREATE DATABASE semi_project;

USE semi_project;

-- 1️⃣ 회원(user)
CREATE TABLE user (
	id INT AUTO_INCREMENT PRIMARY KEY,
	user_id VARCHAR(255) NOT NULL UNIQUE,           -- 로그인용 아이디
	user_pw VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL UNIQUE,
	phone VARCHAR(255) UNIQUE,
	birth VARCHAR(255) NOT NULL,
	gender ENUM('남','여') NOT NULL,
	name VARCHAR(255) NOT NULL,
	nickname VARCHAR(255),
	address VARCHAR(255)
);

-- 2️⃣ 버거(burger)
CREATE TABLE burger (
	id INT AUTO_INCREMENT PRIMARY KEY,
	user_id INT,
	name VARCHAR(255) NOT NULL,
	price INT DEFAULT 0 NOT NULL,
	image_path VARCHAR(1000),
	brand VARCHAR(255),
	patty_type ENUM('치킨','비프','기타'),
	FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 3️⃣ 버거 상세(product_details)
CREATE TABLE product_details (
	id INT AUTO_INCREMENT PRIMARY KEY,
	burger_id INT NOT NULL UNIQUE,   -- burger와 1:1 관계
	calories INT,
	carbohydrates INT,
	protein INT,
	fat INT,
	sodium INT,
	sugar INT,
	allergy_info TEXT,
	FOREIGN KEY (burger_id) REFERENCES burger(id) ON DELETE CASCADE
);

-- 4️⃣ 리뷰(review)
CREATE TABLE review (
	id INT AUTO_INCREMENT PRIMARY KEY,
	burger_id INT,
	user_id INT,
	rating DECIMAL(2,1) CHECK (rating >= 0 AND rating <= 5) NOT NULL,
	content TEXT,
	created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	FOREIGN KEY (burger_id) REFERENCES burger(id) ON DELETE CASCADE,
	FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

-- 5️⃣ 리뷰 이미지(review_image)
CREATE TABLE review_image (
	id INT AUTO_INCREMENT PRIMARY KEY,
	review_id INT,
	image_path VARCHAR(255) NOT NULL,
	FOREIGN KEY (review_id) REFERENCES review(id) ON DELETE CASCADE
);

DESC review;
