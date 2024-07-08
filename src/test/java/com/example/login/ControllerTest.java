package com.example.login;

/*
* Denna kod är en uppsättning enhetstester för AdminController i en Spring-applikation.
* Den använder MockMvc för att simulera HTTP-förfrågningar och kontrollera svaren.
* UserService är mockat för att simulera tjänstbeteenden.
*
* Tester inkluderar att kontrollera om hemsidan visas korrekt beroende på autentisering,
* om registrering lyckas eller misslyckas på grund av valideringsfel,
* och olika scenarier för att radera användare, inklusive framgång, användare ej hittad,
* och försök att radera en administratör.
*
* TestSecurityConfig importeras för säkerhetskonfiguration.
 * */

import com.example.login.config.TestSecurityConfig;
import com.example.login.model.User;
import com.example.login.service.UserService;
import com.example.login.web.AdminController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = AdminController.class)
@Import(TestSecurityConfig.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {
        Mockito.reset(userService); // Återställer mock-objektet före varje test
    }

    @Test
    @WithMockUser
    public void testHomepageAuthenticated() throws Exception {
        mockMvc.perform(get("/homepage"))
                .andExpect(status().isOk())
                .andExpect(view().name("homepage"));
    }

    @Test
    public void testHomepageNotAuthenticated() throws Exception {
        mockMvc.perform(get("/homepage"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void testSuccessfulRegistration() throws Exception {
        mockMvc.perform(post("/register")
                        .param("email", "test@example.com")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register_success"));
    }

    @Test
    @WithMockUser
    public void testRegistrationValidationError() throws Exception {
        mockMvc.perform(post("/register")
                        .param("email", "")
                        .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("register_form"));
    }

    @Test
    @WithMockUser
    public void testDeleteUserSuccess() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setRole("ROLE_USER");
        Mockito.when(userService.findByEmail(anyString())).thenReturn(user);

        mockMvc.perform(post("/delete")
                        .param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("delete_success"));
    }

    @Test
    @WithMockUser
    public void testDeleteUserNotFound() throws Exception {
        Mockito.when(userService.findByEmail(anyString())).thenReturn(null);

        mockMvc.perform(post("/delete")
                        .param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("user_not_found"));
    }

    @Test
    @WithMockUser
    public void testDeleteAdminUser() throws Exception {
        User adminUser = new User();
        adminUser.setEmail("admin@example.com");
        adminUser.setRole("ROLE_ADMIN");
        Mockito.when(userService.findByEmail(anyString())).thenReturn(adminUser);

        mockMvc.perform(post("/delete")
                        .param("email", "admin@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin_error"));
    }


}
