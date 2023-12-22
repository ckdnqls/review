package com.example.review.user.controller;

import com.example.review.common.CommonResponseDto;
import com.example.review.exception.FieldErrorDto;
import com.example.review.exception.FieldErrorException;
import com.example.review.user.dto.LoginRequestDto;
import com.example.review.user.dto.SignupRequestDto;
import com.example.review.user.service.UserService;
import com.example.review.user.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v1/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        // validation 검증
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            List<FieldErrorDto> fieldErrorDtoList =fieldErrors.stream().map(FieldErrorDto::new).toList();
            throw new FieldErrorException("허용된 username 또는 password 값이 아닙니다.", HttpStatus.BAD_REQUEST.value(), fieldErrorDtoList);
        }

        // sevice signup 로직
        userService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(new CommonResponseDto("회원가입 성공",HttpStatus.CREATED.value()));
    }

   @PostMapping("/login")
   public ResponseEntity<CommonResponseDto> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse res) {
       return userService.login(requestDto, res);
   }

    @GetMapping("/logout")
    public ResponseEntity<CommonResponseDto> logout(HttpServletRequest request) {
        userService.logout(request);
        return ResponseEntity.status(HttpStatus.OK.value()).body(new CommonResponseDto("로그아웃 성공",HttpStatus.OK.value()));
    }


}
