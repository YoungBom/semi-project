package model;

public class User {
  private int id;
  private String user_id;
  private String user_pw; // 해시
  private String email;
  private String phone;
  private String birth;
  private String gender;
  private String name;
  private String nickname;
  private String address;

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }

  public String getUser_id() { return user_id; }
  public void setUser_id(String user_id) { this.user_id = user_id; }

  public String getUser_pw() { return user_pw; }
  public void setUser_pw(String user_pw) { this.user_pw = user_pw; }

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
