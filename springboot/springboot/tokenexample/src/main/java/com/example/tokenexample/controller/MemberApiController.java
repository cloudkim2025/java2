package com.example.tokenexample.controller;

import com.example.tokenexample.dto.SignUpRequestDTO;
import com.example.tokenexample.dto.SignUpResponseDTO;
import com.example.tokenexample.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/join")
    public SignUpResponseDTO sign(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        System.out.println(signUpRequestDTO);
        memberService.signUp(signUpRequestDTO.toMember(bCryptPasswordEncoder));
        return SignUpResponseDTO.builder()
                .successd(true)
                .build();
    }
}
