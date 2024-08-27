package be.jimsa.iotproject.ws.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "customers")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(nullable = false, unique = true)
    @JsonProperty("public_id")
    private String publicId;

    @Column(nullable = false)
    private String name;

}
