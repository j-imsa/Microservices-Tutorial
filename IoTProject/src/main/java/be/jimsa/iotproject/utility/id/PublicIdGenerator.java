package be.jimsa.iotproject.utility.id;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class PublicIdGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
    private static final Random RANDOM = new Random();
    private static final int MIN = 32;
    private static final int MAX = 512;
    private static final int DEFAULT_LENGTH = 64;


    public String generatePublicId(int length) {
        if (length < MIN || length > MAX) {
            length = DEFAULT_LENGTH;
        }
        return IntStream.range(0, length)
                .map(i -> RANDOM.nextInt(CHARACTERS.length()))
                .mapToObj(CHARACTERS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
