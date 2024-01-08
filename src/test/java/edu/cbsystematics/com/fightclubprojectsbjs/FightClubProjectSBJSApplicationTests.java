package edu.cbsystematics.com.fightclubprojectsbjs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class FightClubProjectSBJSApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    public void testUnauthorizedHomePageAccess() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/fight-club"))
                    .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                    .andExpect(redirectedUrl("http://localhost/login"));
            System.out.println("Test Unauthorized Home Page Access: Passed!");
        } catch (AssertionError e) {
            fail("Expected unauthorized access to the main page.");
        }
    }

    @Test
    public void testAuthorizedPageAccessForFighter() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/fight-club")
                            .with(SecurityMockMvcRequestPostProcessors.user("fighter").password("123")))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(view().name("view-main-page"));
            System.out.println("Test Authorized Page Access for fighter: Passed!");
        } catch (AssertionError e) {
            fail("Expected authorized access to the main page for fighter.");
        }
    }

    @Test
    public void testAuthorizedPageAccessForPromoter() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/fight-club")
                            .with(SecurityMockMvcRequestPostProcessors.user("promoter").password("123")))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(view().name("view-main-page"));
            System.out.println("Test Authorized Page Access for promoter: Passed!");
        } catch (AssertionError e) {
            fail("Expected authorized access to the main page for promoter.");
        }
    }

    @Test
    public void testAccessToPromoterPage() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/fight-club/admin"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("http://localhost/login"));
            System.out.println("Test Access to Admin Page: Passed!");
        } catch (AssertionError e) {
            fail("Expected redirection to login page for unauthorized access to admin page.");
        }
    }

    @Test
    void loginFighterValidateLogout() throws Exception {
        try {
            // Perform login and validate authentication
            MvcResult mvcResult = mockMvc.perform(formLogin().user("fighter").password("123"))
                    .andExpect(authenticated())
                    .andReturn();

            // Get the session from the login result
            MockHttpSession httpSession = (MockHttpSession) mvcResult.getRequest().getSession(false);

            // Validate logout
            if (httpSession != null) {
                mockMvc.perform(MockMvcRequestBuilders.post("/logout").session(httpSession))
                        .andExpect(unauthenticated())
                        .andExpect(redirectedUrl("/login?logout"));

                // Validate access to a secured resource after logout
                mockMvc.perform(MockMvcRequestBuilders.get("/fight-club/").session(httpSession))
                        .andExpect(unauthenticated())
                        .andExpect(status().is3xxRedirection());
            }
            System.out.println("Test Login, Validate, and Logout for fighter: Passed!");
        } catch (AssertionError e) {
            fail("Expected successful login, authentication, and logout for fighter.");
        }
    }

    @Test
    public void testShowRegistrationForm() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/registration"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("registration"));
            System.out.println("Test Show Registration Form: Passed!");
        } catch (AssertionError e) {
            fail("Expected registration form to be displayed.");
        }
    }

    @Test
    public void testSuccessfulRegistration() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/registration")
                            .param("username", "Test")
                            .param("birthDate", "1990-01-04")
                            .param("email", "test@example.com")
                            .param("password", "qweRTY0-="))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/registration?success"));
            System.out.println("Test Successful Registration: Passed!");
        } catch (AssertionError e) {
            fail("Expected Successful Registration.");
        }

    }

}
