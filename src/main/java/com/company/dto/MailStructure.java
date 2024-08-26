package com.company.dto;

import lombok.Getter;
import lombok.Setter;
// 1
// chứa dữ liệu về nội dung của email
@Getter
@Setter
public class MailStructure {
	private String subject;
	private String message;
}
