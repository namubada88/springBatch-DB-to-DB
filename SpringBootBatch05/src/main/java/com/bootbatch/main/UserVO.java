package com.bootbatch.main;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserVO {
	private int count;
	
	
	public UserVO(int count) {
		this.count = count;
	}
}
