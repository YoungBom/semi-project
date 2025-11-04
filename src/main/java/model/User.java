package model;

public class User {
	private int id;
	private String user_id;
	private String user_pw;
	private String email;
	private String phone;
	private String birth;
	private String gender;
	private String name;
	private String nickname;
	private String address;

	// getter/setter 전부 생성
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String v) {
		this.user_id = v;
	}

	public String getUser_pw() {
		return user_pw;
	}

	public void setUser_pw(String v) {
		this.user_pw = v;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String v) {
		this.email = v;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String v) {
		this.phone = v;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String v) {
		this.birth = v;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String v) {
		this.gender = v;
	}

	public String getName() {
		return name;
	}

	public void setName(String v) {
		this.name = v;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String v) {
		this.nickname = v;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String v) {
		this.address = v;
	}
}
