package dto;

public class UserDTO {
    private int id;
    private String userId;      // 로그인용 아이디
    private String userPw;
    private String email;
    private String phone;
    private String birth;
    private String gender;      // ENUM('남','여')
    private String name;
    private String nickname;
    private String address;

    // 기본 생성자
    public UserDTO() {}

    // getter / setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserPw() { return userPw; }
    public void setUserPw(String userPw) { this.userPw = userPw; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBirth() { return birth; }
    public void setBirth(String birth) { this.birth = birth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
