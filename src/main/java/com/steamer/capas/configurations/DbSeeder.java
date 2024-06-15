package com.steamer.capas.configurations;
import com.steamer.capas.business.service.impl.PasswordEncoderService;
import com.steamer.capas.domain.document.AuthToken;
import com.steamer.capas.domain.document.Game;
import com.steamer.capas.domain.document.Role;
import com.steamer.capas.domain.document.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class DbSeeder implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;
    private final PasswordEncoderService passwordEncoderService;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    public DbSeeder(MongoTemplate mongoTemplate, PasswordEncoderService passwordEncoderService) {
        this.mongoTemplate = mongoTemplate;
        this.passwordEncoderService = passwordEncoderService;
    }

    private static final Map<String, String> MIME_TYPES = new HashMap<>();
    static {
        MIME_TYPES.put("jpg", "image/jpeg");
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("gif", "image/gif");
        MIME_TYPES.put("webp", "image/webp");
    }

    @Override
    public void run(String... args) throws Exception {

        mongoTemplate.dropCollection(User.class);
        mongoTemplate.dropCollection(Game.class);
        mongoTemplate.dropCollection(AuthToken.class);

        User user1 = new User("username1", "user1@example.com", "Country1",
                passwordEncoderService.hashPassword("password1"));
        user1.setRole(Role.USER);
        user1.setAccountEnabled(true);
        user1.getOwnedGames().add("Bitburner");
        user1.getOwnedGames().add("NaissanceE");
        user1.setCartera(65);
        user1.setImage(storeImage("static/image1.png"));

        User user2 = new User("username2", "user2@example.com", "Country2",
                passwordEncoderService.hashPassword("password2"));
        user2.setRole(Role.USER);
        user2.setAccountEnabled(true);
        user2.setImage(storeImage("static/image2.webp"));
        user2.setCartera(35);
        Game game1 = new Game(
                "Ghost of Tsushima",
                "Recorre el camino de Jin Sakai y descubre la experiencia completa de Ghost of Tsushima en esta Versión del Director por primera vez para PC." ,
                "Se avecina una tormenta. Vive la experiencia completa de Ghost of Tsushima: Versión del Director para PC. Forja tu propio camino y descubre las maravillas de este juego de aventura y acción de mundo abierto, a cargo de Sucker Punch Productions, Nixxes Software y PlayStation Studios",
                LocalDate.of(2024, 5, 16),
                "Sucker Punch Productions",
                49.99, Arrays.asList("Action", "Adventure", "Open World"),
                Arrays.asList(
                        "ghost_video1.webm",
                        "ghost_1.jpg",
                        "ghost_2.jpg",
                        "ghost_3.jpg",
                        "ghost_4.jpg"

                ),
                "ghost_header.jpg");

        Game game2 = new Game(
                "EA SPORTS FC 24",
                "UEFA EURO 2024™\n" +
                        "Todo el mundo está incluido, tú también. Lidera a tu país contra los mejores y compite por la gloria internacional en el Juego de todos para conquistar el trono europeo.",
                "EA SPORTS FC™ 24 te da la bienvenida al Juego de Todos: la experiencia futbolística más realista hasta el momento con HyperMotionV, los Estilos de juego optimizados por Opta y un motor Frostbite™ mejorado.",
                LocalDate.of(2023, 8, 29),
                "EA Canada & EA Romania",
                69.99,
                Arrays.asList("Futbol", "Sport", "Multiplayer"),
                Arrays.asList(
                        "fc_video_1.webm",
                        "fc_video_2.webm",
                        "fc_1.jpg",
                        "fc_2.jpg",
                        "fc_3.jpg"
                ),
                "fc_thumbnail.jpg");

        Game game3 = new Game(
                "Elden Ring",
                "Elden Ring is an action role-playing game developed by FromSoftware and published by Bandai Namco Entertainment.",
                "In the game, players take on the role of a Tarnished, a character exiled from the Lands Between, who must traverse the realm to restore the Elden Ring and become the Elden Lord.",
                LocalDate.of(2022, 2, 25),
                "FromSoftware",
                59.99,
                Arrays.asList("RPG", "Fantasy", "Open World"),
                Arrays.asList(
                        "eldenring_video1.webm",
                        "eldenring_gameplay1.jpg",
                        "eldenring_gameplay2.jpg",
                        "eldenring_gameplay3.jpg",
                        "eldenring_gameplay4.jpg"
                ),
                "eldenring_thumbnail.jpg");
        Game game4 = new Game(
                "Cyberpunk 2077",
                "Cyberpunk 2077 es un RPG de aventura y acción de mundo abierto ambientado en la megalópolis de Night City, donde te pondrás en la piel de un mercenario o una mercenaria ciberpunk y vivirás su lucha a vida o muerte por la supervivencia. Mejorado y con contenido nuevo adicional gratuito. Personaliza tu personaje y tu estilo de juego a medida que aceptas trabajos, te labras una reputación y desbloqueas mejoras. Las relaciones que forjes y las decisiones que tomes darán forma al mundo que te rodea. Aquí nacen las leyendas. ¿Cuál será la tuya?\n\nSUMÉRGETE DE LLENO CON LA ACTUALIZACIÓN 2.1\n¡Night City parece más viva que nunca gracias a la actualización gratuita 2.1! Viaja con una red de metro de la NCART totalmente funcional, escucha música mientras exploras la ciudad con tu Radioport, queda con los churris en el apartamento de V, compite en carreras rejugables, conduce nuevos vehículos, disfruta de las mejoras en el manejo y el combate en moto, descubre secretos ocultos y mucho mucho más.\n\nCREA TU PROPIO CIBERPUNK\nConviértete en un forajido urbano equipado con las últimas mejoras cibernéticas y escribe tu leyenda en las calles de Night City.\n\nEXPLORA LA CIUDAD DEL FUTURO\nNight City está repleta de cosas por hacer, lugares que visitar y gente que conocer. Y tú decides dónde ir, cuándo ir y cómo llegar allí.\n\nCONSTRUYE TU LEYENDA\nEmprende audaces aventuras y forja relaciones personales con personajes inolvidables cuyos destinos se decidirán a partir de las decisiones que tomes.\n\nEQUIPADO CON MEJORAS\nVive la experiencia de Cyberpunk 2077 con un montón de cambios y mejoras en el sistema de juego, la economía, la ciudad, el uso del mapa y mucho más.\n\nCONSIGUE OBJETOS EXCLUSIVOS\nConsigue objetos del juego y artículos digitales inspirados por los juegos de CD PROJEKT RED como parte del programa Mis recompensas.",
                "Cyberpunk 2077 es un RPG de aventura y acción de mundo abierto ambientado en el futuro sombrío de Night City, una peligrosa megalópolis obsesionada con el poder, el glamur y las incesantes modificaciones corporales.",
                LocalDate.of(2020, 12, 10),
                "CD PROJEKT RED",
                59.99,
                Arrays.asList("RPG", "Aventura", "Acción", "Mundo Abierto"),
                Arrays.asList(
                        "cyberpunk_video_1.webm",
                        "cyberpunk_video_2.webm",
                        "cyberpunk_1.jpg",
                        "cyberpunk_2.jpg",
                        "cyberpunk_3.jpg"
                ),
                "cyberpunk_thumbnail.jpg"
        );
        Game game5 = new Game(
                "Hellblade: Senua's Sacrifice",
                "De los creadores de Heavenly Sword, Enslaved: Odyssey to the West y DmC: Devil May Cry llega el viaje brutal de una guerrera hacia el mito y la locura.\n\n" +
                        "Ambientado en la era vikinga, una guerrera celta destrozada se embarca en una misión onírica obsesiva para luchar por el alma de su difunto amante.\n\n" +
                        "Creado en colaboración con neurocientíficos y personas que sufren psicosis, Hellblade: El Sacrificio de Senua te sumerge en la mente de Senua.\n\n" +
                        "Características de accesibilidad:\n\n" +
                        "FORMA DE JUEGO\n" +
                        "- Las opciones de accesibilidad están disponibles a partir del menú de arranque.\n" +
                        "- Dificultad de combate ajustable entre el modo \"Automático\", que se ajusta dinámicamente, o los modos \"Fácil\", \"Medio\" o \"Difícil\", que se seleccionan manualmente.\n" +
                        "- Puedes pausar el juego tanto durante la partida como los cinemáticos.\n\n" +
                        "AUDIO\n" +
                        "- Controles de volumen personalizables para el volumen maestro, música, efectos especiales, voz y el audio del menú.\n" +
                        "- La salida de audio se puede ajustar entre el modo estéreo y modo monoaural.\n\n" +
                        "VISUAL\n" +
                        "- Es posible ajustar el modo para daltónicos y la corrección de intensidades para incluir filtros de deuteranopía, protanopía y tritanopía.\n" +
                        "- Es posible activar o desactivar los subtítulos del contenido de los diálogos.\n" +
                        "- Es posible ajustar el tamaño de los subtítulos entre “Estándar” o “Grande”.\n" +
                        "- Es posible cambiar el color de los subtítulos a blanco, anaranjado, verde o azul.\n" +
                        "- Es posible ajustar el fondo de los subtítulos para que sea transparente o completamente negro.\n" +
                        "- Es posible ajustar el contraste del fondo del texto del menú entre “Semitransparente” y “Completamente negro”.\n\n" +
                        "ENTRADAS\n" +
                        "- Es posible ajustar la sensibilidad de las entradas del joystick del control, los ejes del ratón y así como invertir el eje de la cámara.\n" +
                        "- Reasignación de entradas para las teclas del teclado y los botones del control (excepto el control de la cámara y las teclas de navegación del menú).\n" +
                        "- Compatibilidad total con el teclado, lo que permite jugar por completo con el teclado (las teclas de flecha sustituyen al movimiento del ratón o del stick derecho).\n" +
                        "- La vibración del mando se puede activar o desactivar.\n" +
                        "- El modificador de correr puede configurarse para que se active al solo presionar o mantener presionada la tecla/botón.\n\n" +
                        "Ten en cuenta que las opciones de accesibilidad mencionadas se aplican a Hellblade: El sacrificio de Senua mejorado para la versión de PC del juego.",
                "From the makers of Heavenly Sword, Enslaved: Odyssey to the West, and DmC: Devil May Cry, comes a warrior’s brutal journey into myth and madness. Set in the Viking age, a broken Celtic warrior embarks on a haunting vision quest into Viking Hell to fight for the soul of her dead lover.",
                LocalDate.of(2017, 8, 8),
                "Ninja Theory",
                29.99,
                Arrays.asList("Acción", "Aventura", "RPG", "Indie"),
                Arrays.asList(
                        "hellblade_video_1.webm",
                        "hellblade_video_2.webm",
                        "hellblade_1.jpg",
                        "hellblade_2.jpg",
                        "hellblade_3.jpg"
                ),
                "hellblade_thumbnail.jpg"
        );
        Game game6 = new Game(
                "NaissanceE",
                "NaissanceE is an obscure and magical first person exploration game, a philosophical trip and an artistic experience.\n\n" +
                        "The adventure takes place in a primitive mysterious structure and the game mainly consists to explore and feel the deep and strong ambiance of this atemporal world but platforming and puzzles areas will also enrich the experience.\n\n" +
                        "The game is constructed along a linear path punctuated by more open areas to freely explore, some puzzles to solves and some more experimental sequences.\n\n" +
                        "Going deeper and deeper in a primitive zone from “Naissance” world, the player will meet entities or mechanical systems. Whether those entities are life forms or pure machines, they react to player presence, to light and shadow and they may open access to the following.\n\n" +
                        "If most parts of the journey will require only curiosity and logic, a good control and coordination on running, breathing and jumping actions will help to go through rare but exigent sequences, as an homage to old school die an retry games.\n\n" +
                        "The main idea behind the game is to make the player appreciate the loneliness, the feeling to be lost in a gigantic unknown universe and to be marvelled by the beauty of this world. A world which seems to be alive, leading the player, manipulating him and playing with him for any reason.\n\n" +
                        "Imagination is an important key to enjoy and understand NaissanceE. Walking in an undiscovered abstract structure brings questions about the nature of this world, about the meaning of this trip. Evocating and symbolic, the architecture and events will lead player’s imagination to find an answer, if it only matters.\n\n" +
                        "NaissanceE is developed by Limasse Five with the participation of composers Pauline Oliveros, Patricia Dallio and Thierry Zaboitzeff.\n\n" +
                        "Warning! This game is not recommended for people with epilepsy.",
                "NaissanceE is an obscure and magical first person exploration game, a philosophical trip and an artistic experience.",
                LocalDate.now(),  // Assuming the release date is today for simplicity
                "Limasse Five",
                0.0,
                Arrays.asList("Exploration", "Puzzle", "First Person"),
                Arrays.asList("naissancee_image1.jpg", "naissancee_image2.jpg", "naissancee_image3.jpg"),
                "naissancee_thumbnail.jpg");

        Game game7 = new Game(
                "Bitburner",
                "After 5 years of development, contributions from hundreds of developers, the critically acclaimed open source programming hacking sim is available on Steam.\n\n" +
                        "Inspired by games like Else Heart.break(), Hacknet, Uplink, and Deus Ex, Bitburner is a programming-based idle incremental RPG where you, the player, take the role of an unknown hacker in a dark, dystopian world. The game provides a variety of mechanics and systems that can be changed through coding and solved or automated in whatever way you find suitable.\n\n" +
                        "While a very basic programming background is recommended, it is not required to play the game!\n\n" +
                        "Write scripts in JavaScript to automate your gameplay\n" +
                        "Hack through a network of servers to train your abilities and earn money\n" +
                        "Solve real programming questions to hone your skills and earn rewards\n" +
                        "Improve your character with 100+ Augmentations\n" +
                        "Trade in the stock market and write automated trading scripts\n" +
                        "Interact with various gameplay mechanics to increase your stats and earn money\n" +
                        "Explore the world and discover different companies, locations, and factions\n" +
                        "Mini-games\n" +
                        "Unlock secret perma-upgrades\n" +
                        "Continuing development!\n\n" +
                        "This game will be available on linux soon but in the meantime you can play it here https://danielyxie.github.io/bitburner/",
                "Bitburner is a programming-based incremental game. Write scripts in JavaScript to automate gameplay, learn skills, play minigames, solve puzzles, and more in this cyberpunk text-based incremental RPG.",
                LocalDate.now(),  // Assuming the release date is today for simplicity
                "Open Source Community",
                0.0,
                Arrays.asList("Programming", "Incremental", "RPG"),
                Arrays.asList("bitburner_image1.jpg", "bitburner_image2.jpg", "bitburner_image3.jpg", "bitburner_image4.jpg"),
                "bitburner_thumbnail.jpg");

        Game game8 = new Game(
                "Despotism 3k",
                "Humanity is enslaved by an AI… which is awesome, because we’re on the right side of the conflict. Exploit puny humans to extract power and build your own empire! You’ll have to keep track of them to make sure they don’t succumb to exhaustion and hunger… although the weakest ones can always be thrown to the Bioreactor.\n\n" +
                        "In other words, this is a resource management sim with rogue-lite elements, dramatic plot, and an abundance of pop culture references. Also, jokes. Slaughter has never been so fun!\n\n" +
                        "Key features\n" +
                        "Humor as dark as your ex’s heart. Immerse yourself in the life of a merciless tyrant!!\n" +
                        "Resource management on steroids. Think fast, to the point, and abandon pity!\n" +
                        "Rogue-lite. Every walkthrough is unique due to randomly generated events. And every failure is fatal — you’ll have to start over!\n" +
                        "Easy to learn, hard to master. It only takes a couple minutes to figure out the game’s mechanics, but using them optimally is no small feat!",
                "Humanity is enslaved by an AI… which is awesome, because we’re on the right side of the conflict. Exploit puny humans to extract power and build your own empire!",
                LocalDate.now(),
                "Konfa Games",
                11.99,
                Arrays.asList("Resource Management", "Rogue-lite", "Simulation"),
                Arrays.asList("despotism3k_image1.jpg", "despotism3k_image2.jpg", "despotism3k_image3.jpg", "despotism3k_image4.jpg"),
                "despotism3k_thumbnail.jpg");
        // Insert initial data
        mongoTemplate.insert(user1, "users");
        mongoTemplate.insert(user2, "users");
        mongoTemplate.insert(game1, "games");
        mongoTemplate.insert(game2, "games");
        mongoTemplate.insert(game3, "games");
        mongoTemplate.insert(game4, "games");
        mongoTemplate.insert(game5, "games");
        mongoTemplate.insert(game6, "games");
        mongoTemplate.insert(game7, "games");
        mongoTemplate.insert(game8, "games");
    }

    private String storeImage(String imagePath) {
        try {
            ClassPathResource resource = new ClassPathResource(imagePath);
            InputStream inputStream = resource.getInputStream();
            String filename = resource.getFilename();
            String extension = getExtension(filename);
            String contentType = MIME_TYPES.getOrDefault(extension, "application/octet-stream");
            return gridFsTemplate.store(inputStream, filename, contentType).toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex == -1 ? "" : filename.substring(lastDotIndex + 1).toLowerCase();
    }
}