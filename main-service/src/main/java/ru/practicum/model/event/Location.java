package ru.practicum.model.event;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "locations", schema = "public")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location_lat", nullable = false)
    private Double lat;

    @Column(name = "location_lon", nullable = false)
    private Double lon;
}
