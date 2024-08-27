package be.jimsa.iotproject.utility.id;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class PublicIdGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
    private static final Random RANDOM = new Random();

    public String generatePublicId(int length) {
        return IntStream.range(0, length)
                .map(i -> RANDOM.nextInt(CHARACTERS.length()))
                .mapToObj(CHARACTERS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
