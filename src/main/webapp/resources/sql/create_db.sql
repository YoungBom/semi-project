CREATE DATABASE jspbookdb;

SHOW DATABASES;

CREATE DATABASE bookmarketdb;

USE bookmarketdb;

CREATE TABLE IF NOT EXISTS book (
	b_id VARCHAR(10),
	b_name VARCHAR(20),
	b_unitPrice INTEGER,
	b_author VARCHAR(20),
	b_description TEXT,
	b_publisher VARCHAR(20),
	b_category VARCHAR(20),
	b_unitsInStock INTEGER,
	b_releaseDate VARCHAR(20),
	b_condition VARCHAR(20),
	b_fileName VARCHAR(20),
	PRIMARY KEY (b_id)
);


DESC book;
INSERT INTO book (b_id, b_name, b_unitPrice, b_author, b_description, b_publisher, b_category, b_unitsInStock, b_releaseDate, b_condition, b_fileName)

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

-- 우주님이 image_path의 데이터형을 LONGTEXT로 변경하지않으면 인코딩된 image_path가 버거 등록시 들어가지 않는다하심
ALTER TABLE burger
MODIFY COLUMN image_path LONGTEXT NULL;

DESC burger;
SELECT * FROM burger;

-- 3️⃣ 버거 상세(product_details)
CREATE TABLE burger_details (
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

SELECT * FROM user;

INSERT INTO user (user_id, user_pw, email, phone, birth, gender, name, nickname, address)

VALUES
	('ISBN1234', 'C# 프로그래밍', 27000, '우재남', 'C#을 처음 접하는 독자를 대상으로 일대일 수업처럼 자세히 설명한 책이다. 꼭 알아야 할 핵심 개념은 기본 예제로 최대한 쉽게 설명했으며, 중요한 내용은 응용 예제, 퀴즈, 셀프 스터디, 예제 모음으로 한번 더 복습할 수 있다.', '한빛아카데미', 'IT모바일', 1000, '2022/10/06', 'New', 'ISBN1234.jpg'),
	('ISBN1235', '자바마스터', 30000, '송미영', '자바를 처음 배우는 학생을 위해 자바의 기본 개념과 실습 예제를 그림을 이용하여 쉽게 설명합니다. 자바의 이론적 개념→기본 예제→프로젝트 순으로 단계별 학습이 가능하며, 각 챕터의 프로젝트를 실습하면서 온라인 서점을 완성할 수 있도록 구성하였습니다.', '한빛아카데미', 'IT모바일', 1000, '2023/01/01', 'New', 'ISBN1235.jpg'),
	('ISBN1236', '파이썬 프로그래밍', 30000, '최성철', '파이썬으로 프로그래밍을 시작하는 입문자가 쉽게 이해할 수 있도록 기본 개념을 상세하게 설명하며, 다양한 예제를 제시합니다. 또한 프로그래밍의 기초 원리를 이해하면서 파이썬으로 데이터를 처리하는 기법도 배웁니다.', '한빛아카데미', 'IT모바일', 1000, '2023/01/01', 'New', 'ISBN1236.jpg');

SELECT * FROM book;


USE jspbookdb;

CREATE TABLE IF NOT EXISTS member (
	id VARCHAR(20),
  passwd VARCHAR(20),
  name VARCHAR(30),
  PRIMARY KEY (id)
);

SELECT * FROM member;
USE jspbookdb;


USE jspbookdb;

DROP TABLE member;

CREATE TABLE IF NOT EXISTS member (
	id VARCHAR(20),
  passwd VARCHAR(20),
  name VARCHAR(30),
  PRIMARY KEY (id)
);

INSERT INTO member (id, passwd, name) 
VALUES 
	('1', '1234', '홍길순'),
	('2', '1235', '홍길동');

SELECT * FROM member;
USE bookmarketdb;

CREATE TABLE member (
	id VARCHAR(10),
	password VARCHAR(10) NOT NULL,
	name VARCHAR(10) NOT NULL,
	gender VARCHAR(4),
	birth VARCHAR(10),
	mail VARCHAR(30),
	phone VARCHAR(20),
	address VARCHAR(90),
	regist_day VARCHAR(50),
	PRIMARY KEY (id)
);
CREATE TABLE password_reset (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  user_id    INT NOT NULL,
  token      VARCHAR(128) NOT NULL UNIQUE,
  expires_at TIMESTAMP NOT NULL,
  used_at    TIMESTAMP NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_reset_user FOREIGN KEY (user_id) REFERENCES user(id)
);



SELECT * FROM bookmarketdb.member;userPRIMARYuser
DROP TABLE member;
DESC TABLE password_reset;

INSERT INTO burger_details (burger_id, calories, carbohydrates, protein, fat, sodium, sugar, allergy_info)
VALUES
(1, 520, 42, 28, 25, 800, 8, '밀, 우유, 대두'),
(2, 560, 48, 32, 27, 850, 9, '밀, 계란, 대두'),
(3, 620, 50, 35, 30, 900, 10, '밀, 대두'),
(4, 590, 47, 31, 28, 870, 9, '밀, 새우, 대두'),
(5, 650, 49, 37, 33, 920, 11, '밀, 대두, 쇠고기'),
(6, 480, 40, 26, 22, 780, 7, '밀, 우유'),
(7, 500, 42, 29, 24, 800, 8, '밀, 대두, 쇠고기'),
(8, 540, 45, 30, 26, 830, 9, '밀, 닭고기'),
(9, 600, 46, 33, 29, 880, 10, '밀, 새우'),
(10, 670, 48, 38, 34, 940, 12, '밀, 대두, 돼지고기'),
(11, 610, 47, 35, 30, 910, 10, '밀, 대두, 우유'),
(12, 550, 43, 32, 27, 850, 9, '밀, 닭고기'),
(13, 820, 52, 41, 38, 970, 12, '밀, 쇠고기'),
(14, 780, 50, 39, 36, 940, 11, '밀, 쇠고기, 우유'),
(15, 560, 44, 31, 27, 850, 8, '밀, 새우'),
(16, 750, 51, 38, 34, 930, 10, '밀, 쇠고기'),
(17, 580, 45, 32, 28, 860, 9, '밀, 닭고기'),
(18, 740, 50, 37, 35, 920, 10, '밀, 쇠고기, 우유'),
(19, 710, 48, 36, 33, 910, 9, '밀, 새우, 쇠고기'),
(20, 770, 52, 39, 36, 940, 11, '밀, 우유'),
(21, 900, 54, 43, 40, 1000, 12, '밀, 쇠고기'),
(22, 680, 49, 35, 32, 880, 9, '밀, 닭고기'),
(23, 760, 50, 38, 34, 920, 10, '밀, 우유'),
(24, 620, 46, 32, 28, 870, 9, '밀, 쇠고기'),
(25, 590, 45, 30, 26, 850, 8, '밀, 계란'),
(26, 610, 47, 33, 29, 870, 9, '밀, 닭고기'),
(27, 720, 50, 37, 33, 910, 10, '밀, 새우'),
(28, 640, 48, 34, 30, 880, 9, '밀, 닭고기'),
(29, 700, 50, 36, 32, 920, 10, '밀, 우유'),
(30, 890, 53, 42, 39, 990, 12, '밀, 쇠고기');

