package model;

public class User {
	private int id; // PK(INT)
	private String user_id; // 로그인 아이디
	private String user_pw; // 비밀번호
	private String email;   // 이메일
	private String phone;   // 전화번호
	private String birth;   // 생년월일
	private String gender;  // 성별
	private String name;    // 이름
	private String nickname; // 닉네임
	private String address; // 주소

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_pw() {
		return user_pw;
	}

	public void setUser_pw(String user_pw) {
		this.user_pw = user_pw;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	// --- 별칭(서블릿 호환용 ---
	public String getPassword() {
		return user_pw;
	}

	public void setPassword(String hashed) {
		this.user_pw = hashed;
	}

	public void setLoginId(String v) {
		this.user_id = v;
	}

	public String getLoginId() {
		return this.user_id;
	}
}
