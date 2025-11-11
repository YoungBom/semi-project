package dto;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class UserDTO {
	// 기본 키는 Long으로 (DAO에서 rs.getLong("id") 사용과 호환)
	private Long id;

	private String userId;
	private String pwHash;
	private String userPw; // legacy/plain (nullable)
	private String email;
	private String phone;
	private LocalDate birth;
	private String gender; // "남"|"여"
	private String name;
	private String nickname;
	private String address;
	private String role;

	private Timestamp createdAt;
	private Timestamp updatedAt;

	// --- getters / setters ---

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// (선택) int 소스와도 호환되게 오버로드 하나 더
	public void setId(int id) {
		this.id = Long.valueOf(id);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPwHash() {
		return pwHash;
	}

	public void setPwHash(String pwHash) {
		this.pwHash = pwHash;
	}

	public String getUserPw() {
		return userPw;
	}

	public void setUserPw(String userPw) {
		this.userPw = userPw;
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

	public LocalDate getBirth() {
		return birth;
	}

	public void setBirth(LocalDate birth) {
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp ts) {
		this.createdAt = ts;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp ts) {
		this.updatedAt = ts;
	} // ← 기존 버그 수정: updatedAt에 세팅

	// --- 유틸 ---

	public static LocalDate toLocalDate(Date date) {
		if (date == null)
			return null;
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	// --- 호환용 별칭 (snake_case 필드명을 쓰는 기존 코드/DAO와 동시 호환) ---

	// user_id
	public String getUser_id() {
		return getUserId();
	}

	public void setUser_id(String v) {
		setUserId(v);
	}

	// pw_hash
	public String getPw_hash() {
		return getPwHash();
	}

	public void setPw_hash(String v) {
		setPwHash(v);
	}

	// created_at / updated_at
	public Timestamp getCreated_at() {
		return getCreatedAt();
	}

	public void setCreated_at(Timestamp v) {
		setCreatedAt(v);
	}

	public Timestamp getUpdated_at() {
		return getUpdatedAt();
	}

	public void setUpdated_at(Timestamp v) {
		setUpdatedAt(v);
	}
}
