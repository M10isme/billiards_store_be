package com.example.billiardsstore.dto.contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContactRequest {
    @NotBlank(message = "Họ và tên không được để trống")
    @Size(min = 2, max = 100, message = "Họ và tên phải từ 2-100 ký tự")
    private String name;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Size(min = 10, max = 15, message = "Số điện thoại không hợp lệ")
    private String phone;

    @NotBlank(message = "Chủ đề không được để trống")
    @Size(max = 200, message = "Chủ đề không được quá 200 ký tự")
    private String subject;

    @NotBlank(message = "Nội dung tin nhắn không được để trống")
    @Size(min = 10, max = 1000, message = "Nội dung tin nhắn phải từ 10-1000 ký tự")
    private String message;
}