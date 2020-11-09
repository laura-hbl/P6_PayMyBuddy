package com.paymybuddy.paymybuddy.integration;

import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource({"/application-test.properties"})
@Sql(scripts = "/schema-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Tag("FindByEmail")
    public void givenARegisteredUserEmail_whenFindByEmail_thenReturnTheRegisteredUser() {
        User user = userRepository.findByEmail("brad@gmail.com");

        assertThat(user.getLastName()).isEqualTo("Pitt");
        assertThat(user.getPhone()).isEqualTo("111-111-111");
    }

    @Test
    @Tag("FindByEmail - Exception")
    public void givenAnUnRegisteredUserEmail_whenFindByEmail_thenReturnNull() {
        User user = userRepository.findByEmail("foo@gmail.com");

        assertThat(user).isEqualTo(null);
    }
}
