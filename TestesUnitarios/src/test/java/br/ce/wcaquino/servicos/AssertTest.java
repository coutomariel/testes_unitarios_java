package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Usuario;
import org.junit.Test;

import static org.junit.Assert.*;

public class AssertTest {

    @Test
    public void teste(){
        assertTrue(true);
        assertFalse(false);

        assertEquals(1,1);
        assertEquals(0.5132,0.51323, 0.001);
        assertEquals(Math.PI,3.14, 0.01);

        int v1 = 1;
        Integer v2 = 1;
        assertEquals(v1, v2.intValue());
        assertEquals(Integer.valueOf(v1), v2);


        assertEquals("bola", "bola");
        assertTrue("bola".equalsIgnoreCase("Bola"));
        assertTrue("bola".startsWith("bo"));

        Usuario user1 = new Usuario("Mariel");
        Usuario user2 = new Usuario("mariel");
        Usuario user3 = null;

        assertNotEquals(user1, user2);
        assertEquals(user1, user1);
        assertNull(user3);
    }
}
