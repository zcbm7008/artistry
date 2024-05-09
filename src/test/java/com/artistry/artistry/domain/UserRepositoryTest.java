package com.artistry.artistry.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("유저 더미 데이터를 확인한다.")
    @ParameterizedTest
    @CsvSource({"1, user1","2, user2"})
    void dummyUserTest(Long id, String name){
        Optional<User> user = userRepository.findById(id);
        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getName()).isEqualTo(name);
    }
}
