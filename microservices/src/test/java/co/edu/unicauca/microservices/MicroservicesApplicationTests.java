package co.edu.unicauca.microservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import co.edu.unicauca.microservices.pkgQuestionService.clsControllerQuestionSrv;
import co.edu.unicauca.microservices.pkgQuestionService.clsQuestion;
import co.edu.unicauca.microservices.pkgUserService.IRoleRepository;
import co.edu.unicauca.microservices.pkgUserService.IUserRepository;
import co.edu.unicauca.microservices.pkgUserService.clsControllerUserSvr;
import co.edu.unicauca.microservices.pkgUserService.clsRole;
import co.edu.unicauca.microservices.pkgUserService.clsUser;

@SpringBootTest
class MicroservicesApplicationTests {

    @Autowired
    private clsControllerUserSvr userController;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private clsControllerQuestionSrv questionController;

    @Test
    public void testAllCrudsUsersAndQuestions() {
        // =========================================================================
        // PARTE 1: CRUD DE ROLES Y USUARIOS (pkgUserService)
        // =========================================================================
        System.out.println("=== 1. PROBANDO CRUD DE ROLES Y USUARIOS ===");

        // 1.1 CREATE ROLES
        clsRole rolAdmin = new clsRole("ROL_01", "Administrador");
        clsRole rolDocente = new clsRole("ROL_02", "Docente");
        userController.opCreateRole(rolAdmin);
        userController.opCreateRole(rolDocente);

        List<clsRole> listaRoles = userController.opListAllRoles();
        assertTrue(listaRoles.size() >= 2, "Deberían existir al menos 2 roles");

        // 1.2 CREATE USER
        Set<clsRole> rolesAsignados = new HashSet<>();
        rolesAsignados.add(rolAdmin);
        rolesAsignados.add(rolDocente);

        clsUser nuevoUsuario = new clsUser("USR_01", "fredy", "clave123", rolesAsignados);
        clsUser usuarioCreado = userController.opCreate(nuevoUsuario);
        assertNotNull(usuarioCreado);
        assertEquals("USR_01", usuarioCreado.getOUID());

        // 1.3 READ USER (Por OUID y Listar Todos)
        clsUser usuarioConsultado = userController.opGetForOUID("USR_01");
        assertNotNull(usuarioConsultado);
        assertEquals("fredy", usuarioConsultado.getName());
        assertEquals(2, usuarioConsultado.getRoles().size());

        List<clsUser> todosLosUsuarios = userController.opListAll();
        assertTrue(todosLosUsuarios.size() >= 1);

        // 1.4 UPDATE USER
        clsUser datosUsuarioActualizados = new clsUser("USR_01", "fredy_editado", "nuevaClave999", rolesAsignados);
        clsUser usuarioEditado = userController.opUpdate("USR_01", datosUsuarioActualizados);
        assertEquals("fredy_editado", usuarioEditado.getName());

        // 1.5 DELETE USER & ROLES
        userController.opDelete("USR_01");
        assertNull(userController.opGetForOUID("USR_01"), "El usuario USR_01 debe haberse eliminado");
        roleRepository.deleteById("ROL_01");
        roleRepository.deleteById("ROL_02");
        System.out.println("-> CRUD de Usuarios y Roles OK.");

        // =========================================================================
        // PARTE 2: CRUD DE PREGUNTAS (pkgQuestionService)
        // =========================================================================
        System.out.println("=== 2. PROBANDO CRUD DE PREGUNTAS ===");

        // 2.1 CREATE QUESTION
        // Usamos el constructor vacío y setters para adaptarnos a tus atributos en
        // clsQuestion
        clsQuestion nuevaPregunta = new clsQuestion();
        nuevaPregunta.setOUID("QST_01");
        // Si tienes más atributos en clsQuestion (ej. enunciado, respuesta), puedes
        // asignarlos aquí:
        // nuevaPregunta.setAttStatement("¿Qué es un microservicio?");

        clsQuestion preguntaCreada = questionController.opCreate(nuevaPregunta);
        assertNotNull(preguntaCreada, "La pregunta creada no debe ser nula");
        assertEquals("QST_01", preguntaCreada.getOUID());
        System.out.println("-> Pregunta creada con OUID: " + preguntaCreada.getOUID());

        // 2.2 READ QUESTION (Por OUID y Listar Todas)
        clsQuestion preguntaConsultada = questionController.opGetForOUID("QST_01");
        assertNotNull(preguntaConsultada, "La pregunta QST_01 debe existir en la BD");
        assertEquals("QST_01", preguntaConsultada.getOUID());

        List<clsQuestion> todasLasPreguntas = questionController.opListAll();
        assertTrue(todasLasPreguntas.size() >= 1, "La lista de preguntas no debe estar vacía");
        System.out.println("-> Total preguntas en BD: " + todasLasPreguntas.size());

        // 2.3 UPDATE QUESTION
        clsQuestion preguntaActualizar = new clsQuestion();
        preguntaActualizar.setOUID("QST_01");
        // Si tienes otro atributo para editar, cámbialo aquí antes de llamar a
        // opUpdate:
        // preguntaActualizar.setAttStatement("Enunciado actualizado");

        clsQuestion preguntaEditada = questionController.opUpdate("QST_01", preguntaActualizar);
        assertNotNull(preguntaEditada);
        assertEquals("QST_01", preguntaEditada.getOUID());
        System.out.println("-> Pregunta actualizada correctamente.");

        // 2.4 DELETE QUESTION
        questionController.opDelete("QST_01");
        clsQuestion preguntaBorrada = questionController.opGetForOUID("QST_01");
        assertNull(preguntaBorrada, "La pregunta QST_01 ya no debería existir tras opDelete");
        System.out.println("-> Pregunta QST_01 eliminada correctamente.");

        System.out.println("=== ¡TODOS LOS CRUDS (USUARIOS, ROLES Y PREGUNTAS) FUNCIONAN AL 100%! ===");
    }

    @Test
    public void testObtenerPreguntasPorUsuario() {
        System.out.println("=== INICIANDO PRUEBA: obtenerPreguntasPorUsuario ===");

        String usuarioBuscado = "USR_DOCENTE_01";
        String otroUsuario = "USR_DOCENTE_02";

        // 1. Creamos 2 preguntas para USR_DOCENTE_01 y 1 pregunta para USR_DOCENTE_02
        clsQuestion pregunta1 = new clsQuestion();
        pregunta1.setOUID("QST_USR_01");
        pregunta1.setQuestionUser(usuarioBuscado);

        clsQuestion pregunta2 = new clsQuestion();
        pregunta2.setOUID("QST_USR_02");
        pregunta2.setQuestionUser(usuarioBuscado);

        clsQuestion preguntaOtro = new clsQuestion();
        preguntaOtro.setOUID("QST_USR_03");
        preguntaOtro.setQuestionUser(otroUsuario);

        questionController.opCreate(pregunta1);
        questionController.opCreate(pregunta2);
        questionController.opCreate(preguntaOtro);

        // 2. Ejecutamos tu función obtenerPreguntasPorUsuario para "USR_DOCENTE_01"
        List<clsQuestion> preguntasDelUsuario = questionController.opGetQuestionsUser(usuarioBuscado);

        // 3. Verificamos que devuelva exactamente las 2 preguntas de ese usuario y no
        // la del otro
        assertNotNull(preguntasDelUsuario, "La lista no debe ser nula");
        assertEquals(2, preguntasDelUsuario.size(), "Debería encontrar exactamente 2 preguntas para " + usuarioBuscado);
        assertTrue(preguntasDelUsuario.stream().allMatch(q -> usuarioBuscado.equals(q.getQuestionUser())),
                "Todas las preguntas recuperadas deben pertenecer a " + usuarioBuscado);

        System.out.println("-> Preguntas encontradas para " + usuarioBuscado + ": " + preguntasDelUsuario.size());
        preguntasDelUsuario.forEach(
                q -> System.out.println("   * Pregunta OUID: " + q.getOUID() + " | Usuario: " + q.getQuestionUser()));

        // 4. Limpiamos las preguntas de prueba de la base de datos
        questionController.opDelete("QST_USR_01");
        questionController.opDelete("QST_USR_02");
        questionController.opDelete("QST_USR_03");

        System.out.println("=== PRUEBA obtenerPreguntasPorUsuario FINALIZADA CON ÉXITO ===");
    }

    @Test
    public void testBuscarUsuarioPorNombre() {
        System.out.println("=== INICIANDO PRUEBA: opGetUserforName ===");

        // "testjuan" es un texto poco comun para no mezclarse con datos reales de la BD
        userController.opCreate(new clsUser("USR_BUS_01", "TestJuan Perez", "clave1", new HashSet<>()));
        userController.opCreate(new clsUser("USR_BUS_02", "testjuana Lopez", "clave2", new HashSet<>()));
        userController.opCreate(new clsUser("USR_BUS_03", "TestCarlos Gomez", "clave3", new HashSet<>()));

        try {
            // 1. Coincidencia parcial: debe traer solo a los dos "testjuan..."
            List<clsUser> resultado = userController.opGetUserforName("testjuan");
            assertNotNull(resultado, "La lista no debe ser nula");
            assertEquals(2, resultado.size(), "Deberia encontrar exactamente 2 usuarios con 'testjuan'");
            assertTrue(resultado.stream().anyMatch(u -> "USR_BUS_01".equals(u.getOUID())));
            assertTrue(resultado.stream().anyMatch(u -> "USR_BUS_02".equals(u.getOUID())));

            // 2. Ignora mayusculas/minusculas
            List<clsUser> resultadoMayus = userController.opGetUserforName("TESTCARLOS");
            assertEquals(1, resultadoMayus.size());
            assertEquals("TestCarlos Gomez", resultadoMayus.get(0).getName());

            // 3. Sin coincidencias: lista vacia
            List<clsUser> resultadoVacio = userController.opGetUserforName("zzzNoExiste");
            assertTrue(resultadoVacio.isEmpty(), "No deberia encontrar usuarios");

            System.out.println("=== PRUEBA opGetUserforName FINALIZADA CON EXITO ===");
        } finally {
            // Limpieza aunque el test falle, para no dejar datos en la BD
            userController.opDelete("USR_BUS_01");
            userController.opDelete("USR_BUS_02");
            userController.opDelete("USR_BUS_03");
        }
    }

    @Test
    public void testBuscarRolPorNombre() {
        System.out.println("=== INICIANDO PRUEBA: opGetRoleforName ===");

        userController.opCreateRole(new clsRole("ROL_BUS_01", "TestAdministrador"));
        userController.opCreateRole(new clsRole("ROL_BUS_02", "testadmin Suplente"));
        userController.opCreateRole(new clsRole("ROL_BUS_03", "TestDocente"));

        try {
            // 1. Coincidencia parcial
            List<clsRole> resultado = userController.opGetRoleforName("testadmin");
            assertEquals(2, resultado.size(), "Deberia encontrar 2 roles con 'testadmin'");
            assertTrue(resultado.stream().anyMatch(r -> "ROL_BUS_01".equals(r.getOUID())));
            assertTrue(resultado.stream().anyMatch(r -> "ROL_BUS_02".equals(r.getOUID())));

            // 2. Ignora mayúsculas
            List<clsRole> resultadoMayus = userController.opGetRoleforName("TESTDOCENTE");
            assertEquals(1, resultadoMayus.size());
            assertEquals("TestDocente", resultadoMayus.get(0).getName());

            // 3. Sin coincidencias
            assertTrue(userController.opGetRoleforName("zzzNoExiste").isEmpty());

            System.out.println("=== PRUEBA opGetRoleforName FINALIZADA CON EXITO ===");
        } finally {
            roleRepository.deleteById("ROL_BUS_01");
            roleRepository.deleteById("ROL_BUS_02");
            roleRepository.deleteById("ROL_BUS_03");
        }
    }
}