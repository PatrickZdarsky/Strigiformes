package at.rxcki.strigiformes.parser.token;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Patrick Zdarsky / Rxcki
 */
class TokenFactoryTest {


    @Test
    void testColorToken() {
        assertEquals(ColorToken.class, TokenFactory.getToken('§', null, 0).getClass());
    }

    @Test
    void testVariableToken() {
        assertEquals(VariableToken.class, TokenFactory.getToken('$', null, 0).getClass());
    }

    @Test
    void testComponentToken() {
        assertEquals(ComponentToken.class, TokenFactory.getToken('%', null, 0).getClass());
    }

    @Test
    void testInvalidToken() {
        assertNull(TokenFactory.getToken('k', null, 0));
    }
}