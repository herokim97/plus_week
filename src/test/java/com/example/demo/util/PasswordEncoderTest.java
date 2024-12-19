package com.example.demo.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderTest {

    @Test
    @DisplayName("encoding Test")
    void encode() {
        // given
        String rawPassword = "123456";


        String encodedPassword = PasswordEncoder.encode(rawPassword);

        assertNotEquals(rawPassword, encodedPassword, "인코딩 된 비밀번호와 같은 출력이 아닙니다.");
    }

    @Test
    @DisplayName("비밀번호 인코딩 후, 매칭 테스트")
    void matchedPassword() {
        String rawPassword = "123456";
        String encodedPassword = PasswordEncoder.encode(rawPassword);

        boolean matched = PasswordEncoder.matches(rawPassword, encodedPassword);

        assertTrue(matched);
    }

    @Test
    @DisplayName("비밀번호 인코딩 후, 매칭 실패 테스트")
    void notMatchedPassword() {
        String rawPassword = "123456";
        String otherPassword = "4567";
        String encodedPassword = PasswordEncoder.encode(rawPassword);

        boolean matched = PasswordEncoder.matches(otherPassword, encodedPassword);

        assertFalse(matched);
    }

}