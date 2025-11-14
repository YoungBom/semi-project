-- Active: 1761795899147@@127.0.0.1@3306@semi_project
CREATE DATABASE semi_project;

USE semi_project;

-- 1️⃣ 회원(user)
CREATE TABLE user (
	id INT AUTO_INCREMENT PRIMARY KEY,
	user_id VARCHAR(255) NOT NULL UNIQUE, -- 로그인용 아이디
	pw_hash VARCHAR(255) NOT NULL, -- 비밀번호를 보안을 위해 해시값으로 쓴다.
	user_pw VARCHAR(255) DEFAULT NULL, -- 현재는 pw_hash로 대체되어서 굳이 필요없는 컬럼이긴한데 일단 냅둠
	email VARCHAR(255) NOT NULL UNIQUE,
	phone VARCHAR(255) UNIQUE,
	birth VARCHAR(255) NOT NULL,
	gender ENUM('남','여') NOT NULL,
	name VARCHAR(255) NOT NULL,
	nickname VARCHAR(8),
	address VARCHAR(255),
    profile_image VARCHAR(1024), -- 유저 테이블 프로필 사진 저장 컬럼
	role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


INSERT INTO user (user_id, pw_hash, user_pw, email, phone, birth, gender, name, nickname, address)
VALUES
('user01', 'hashpw01', 'pw01', 'user01@email.com', '010-1111-0001', '1995-01-01', '남', '홍길동', '길동이', '서울시 강남구'),
('user02', 'hashpw02', 'pw02', 'user02@email.com', '010-1111-0002', '1994-02-02', '여', '김영희', '희희', '서울시 송파구'),
('user03', 'hashpw03', 'pw03', 'user03@email.com', '010-1111-0003', '1990-03-03', '남', '이철수', '철수짱', '부산시 해운대구'),
('user04', 'hashpw04', 'pw04', 'user04@email.com', '010-1111-0004', '1993-04-04', '여', '박민정', '밍밍', '대구시 수성구'),
('user05', 'hashpw05', 'pw05', 'user05@email.com', '010-1111-0005', '1991-05-05', '남', '최진호', '진호맨', '광주시 북구'),
('user06', 'hashpw06', 'pw06', 'user06@email.com', '010-1111-0006', '1992-06-06', '여', '정은지', '은지짱', '인천시 연수구'),
('user07', 'hashpw07', 'pw07', 'user07@email.com', '010-1111-0007', '1998-07-07', '남', '오세훈', '훈이', '서울시 노원구'),
('user08', 'hashpw08', 'pw08', 'user08@email.com', '010-1111-0008', '1999-08-08', '여', '장예진', '예진이', '서울시 은평구'),
('user09', 'hashpw09', 'pw09', 'user09@email.com', '010-1111-0009', '2000-09-09', '남', '김민수', '민수킹', '대전시 서구'),
('user10', 'hashpw10', 'pw10', 'user10@email.com', '010-1111-0010', '1997-10-10', '여', '이유리', '유리별', '서울시 강서구'),
('user11', 'hashpw11', 'pw11', 'user11@email.com', '010-1111-0011', '1991-01-15', '남', '한상우', '상우', '부산시 남구'),
('user12', 'hashpw12', 'pw12', 'user12@email.com', '010-1111-0012', '1993-02-16', '여', '문지현', '지현짱', '서울시 동작구'),
('user13', 'hashpw13', 'pw13', 'user13@email.com', '010-1111-0013', '1992-03-17', '남', '조민석', '민석이', '인천시 미추홀구'),
('user14', 'hashpw14', 'pw14', 'user14@email.com', '010-1111-0014', '1994-04-18', '여', '서예린', '예린', '서울시 용산구'),
('user15', 'hashpw15', 'pw15', 'user15@email.com', '010-1111-0015', '1995-05-19', '남', '백승호', '승호', '경기도 성남시'),
('user16', 'hashpw16', 'pw16', 'user16@email.com', '010-1111-0016', '1996-06-20', '여', '이민아', '미나', '경기도 수원시'),
('user17', 'hashpw17', 'pw17', 'user17@email.com', '010-1111-0017', '1997-07-21', '남', '정도윤', '도윤', '부산시 동래구'),
('user18', 'hashpw18', 'pw18', 'user18@email.com', '010-1111-0018', '1998-08-22', '여', '윤소희', '소희', '서울시 마포구'),
('user19', 'hashpw19', 'pw19', 'user19@email.com', '010-1111-0019', '1999-09-23', '남', '강하준', '하준', '대구시 달서구'),
('user20', 'hashpw20', 'pw20', 'user20@email.com', '010-1111-0020', '2000-10-24', '여', '이서연', '서연', '광주시 서구'),
('user21', 'hashpw21', 'pw21', 'user21@email.com', '010-1111-0021', '1995-11-01', '남', '김준호', '준호', '서울시 중구'),
('user22', 'hashpw22', 'pw22', 'user22@email.com', '010-1111-0022', '1994-12-02', '여', '최수연', '수연', '서울시 강동구'),
('user23', 'hashpw23', 'pw23', 'user23@email.com', '010-1111-0023', '1993-11-03', '남', '박지호', '지호', '인천시 서구'),
('user24', 'hashpw24', 'pw24', 'user24@email.com', '010-1111-0024', '1992-10-04', '여', '오하늘', '하늘', '부산시 해운대구'),
('user25', 'hashpw25', 'pw25', 'user25@email.com', '010-1111-0025', '1991-09-05', '남', '류시원', '시원', '서울시 서초구'),
('user26', 'hashpw26', 'pw26', 'user26@email.com', '010-1111-0026', '1990-08-06', '여', '배은영', '은영', '대전시 중구'),
('user27', 'hashpw27', 'pw27', 'user27@email.com', '010-1111-0027', '1989-07-07', '남', '임성훈', '성훈', '서울시 관악구'),
('user28', 'hashpw28', 'pw28', 'user28@email.com', '010-1111-0028', '1988-06-08', '여', '조하나', '하나', '서울시 종로구'),
('user29', 'hashpw29', 'pw29', 'user29@email.com', '010-1111-0029', '1987-05-09', '남', '노태현', '태현', '부산시 북구'),
('user30', 'hashpw30', 'pw30', 'user30@email.com', '010-1111-0030', '1986-04-10', '여', '윤지민', '지민', '경기도 고양시');


-- 안전모드 OFF
SET SQL_SAFE_UPDATES = 0;

-- 만약 user테이블에서 user_pw값이 있다면 pw_hash로 그 값을 복사
UPDATE `user`
   SET `pw_hash` = `user_pw`
 WHERE `pw_hash` IS NULL AND `user_pw` IS NOT NULL;
 
-- 안전모드 ON
SET SQL_SAFE_UPDATES = 1;

-- 모드변환 코드    USER <---> ADMIN
-- UPDATE user SET role = "ADMIN" WHERE id = '로그인한 계정의 id';
-- 예) UPDATE user SET role = "ADMIN" WHERE id = '4';
-- UPDATE user SET role = "ADMIN" WHERE user_id = '로그인한 계정의 user_id';
-- 예) UPDATE user SET role = "USER" WHERE user_id = 'user02';

-- 2️⃣ 버거(burger)
CREATE TABLE burger (
	id INT AUTO_INCREMENT PRIMARY KEY,
	user_id INT NULL, -- 외래키 null값으로 지정
	name VARCHAR(255) NOT NULL,
	price INT NOT NULL DEFAULT 0,
	image_path LONGTEXT, -- 우주님이 image_path의 데이터형을 LONGTEXT로 변경하지않으면 인코딩된 image_path가 버거 등록시 들어가지 않는다하심
	brand VARCHAR(255),
	patty_type ENUM('치킨','비프','기타'),
    is_new BOOLEAN DEFAULT FALSE,
	FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE SET NULL -- 참조하고 있는 user(id) 행이 삭제되면 → 현재 테이블의 user_id 값을 NULL로 바꿔라
);

INSERT INTO burger (user_id, name, price, image_path, brand, patty_type)
VALUES
(1, '빅맥', 5500, '/img/bigmac.jpg', '맥도날드', '비프'),
(2, '상하이 스파이시 버거', 5800, '/img/shanghai.jpg', '맥도날드', '치킨'),
(3, '와퍼', 6500, '/img/whopper.jpg', '버거킹', '비프'),
(4, '통새우버거', 6000, '/img/shrimp.jpg', '롯데리아', '기타'),
(5, '한우불고기버거', 7000, '/img/bulgogi.jpg', '맥도날드', '비프'),
(6, '치즈버거', 4800, '/img/cheese.jpg', '버거킹', '비프'),
(7, '불고기버거', 5200, '/img/bulgogi_burger.jpg', '롯데리아', '비프'),
(8, '치킨버거', 5300, '/img/chicken.jpg', '버거킹', '치킨'),
(9, '슈퍼새우버거', 6700, '/img/super_shrimp.jpg', '맥도날드', '기타'),
(10, '베이컨치즈버거', 6900, '/img/bacon_cheese.jpg', '버거킹', '비프'),
(11, '치즈불고기버거', 6400, '/img/cheese_bulgogi.jpg', '롯데리아', '비프'),
(12, '스파이시치킨버거', 5900, '/img/spicy_chicken.jpg', '버거킹', '치킨'),
(13, '더블와퍼', 8000, '/img/double_whopper.jpg', '버거킹', '비프'),
(14, '치즈스테이크버거', 7800, '/img/cheese_steak.jpg', '맥도날드', '비프'),
(15, '새우버거', 5500, '/img/shrimp_burger.jpg', '롯데리아', '기타'),
(16, '화이트갈릭버거', 7500, '/img/white_garlic.jpg', '버거킹', '비프'),
(17, '치킨텐더버거', 5700, '/img/chicken_tender.jpg', '맥도날드', '치킨'),
(18, '치즈더블버거', 7400, '/img/cheese_double.jpg', '버거킹', '비프'),
(19, '슈비버거', 7100, '/img/shrimp_beef.jpg', '맥도날드', '기타'),
(20, '콰트로치즈버거', 7700, '/img/4cheese.jpg', '버거킹', '비프'),
(21, '와규버거', 9000, '/img/wagyu.jpg', '롯데리아', '비프'),
(22, '더블치킨버거', 6800, '/img/double_chicken.jpg', '버거킹', '치킨'),
(23, '치즈킹버거', 7600, '/img/cheese_king.jpg', '버거킹', '비프'),
(24, '비프불고기버거', 6200, '/img/beef_bulgogi.jpg', '맥도날드', '비프'),
(25, '베이컨에그버거', 5900, '/img/bacon_egg.jpg', '롯데리아', '비프'),
(26, '클래식치킨버거', 6100, '/img/classic_chicken.jpg', '버거킹', '치킨'),
(27, '더블새우버거', 7200, '/img/double_shrimp.jpg', '맥도날드', '기타'),
(28, '크리스피치킨버거', 6300, '/img/crispy_chicken.jpg', '롯데리아', '치킨'),
(29, '치즈와퍼', 7000, '/img/cheese_whopper.jpg', '버거킹', '비프'),
(30, '트리플버거', 8900, '/img/triple_burger.jpg', '맥도날드', '비프');


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
	review_id INT NOT NULL,
	image_path VARCHAR(255),
	FOREIGN KEY (review_id) REFERENCES review(id) ON DELETE CASCADE
);

CREATE TABLE board (
    board_id int AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content MEDIUMTEXT,
    writer_id VARCHAR(50) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    view_count INT DEFAULT '0',
    category VARCHAR(30) DEFAULT '자유',
    writer_nickname VARCHAR(255) DEFAULT NULL
);


CREATE TABLE IF NOT EXISTS security_question (
  id INT AUTO_INCREMENT PRIMARY KEY,
  question_text VARCHAR(255) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT 1
);

-- INSERT IGNORE → 같은 id가 이미 있으면 에러 없이 무시
INSERT IGNORE INTO security_question (id, question_text, active) VALUES
  (1, '가장 기억에 남는 초등학교 선생님 성함은?', 1),
  (2, '처음 키운 반려동물의 이름은?', 1),
  (3, '내가 가장 좋아하는 도시 이름은?', 1),
  (4, '어머니의 출생지는?', 1),
  (5, '내가 처음으로 다닌 회사 이름은?', 1);
  
CREATE TABLE IF NOT EXISTS security_answer (
  user_id     INT          NOT NULL PRIMARY KEY,
  question_id INT      NOT NULL,
  question_tx VARCHAR(200) NULL,
  answer_hash VARCHAR(255) NOT NULL,
  created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_security_qa_user     FOREIGN KEY (user_id)     REFERENCES user(id) ON DELETE CASCADE,
  CONSTRAINT fk_security_qa_question FOREIGN KEY (question_id) REFERENCES security_question(id)
);