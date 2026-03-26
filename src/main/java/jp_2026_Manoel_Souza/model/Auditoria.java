package jp_2026_Manoel_Souza.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auditorias")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String entidade;

    @Column(name = "entidade_id", nullable = false)
    private Long entidadeId;

    @Column(nullable = false, length = 30)
    private String acao;

    @Lob
    @Column(name = "antes_json")
    private String antesJson;

    @Lob
    @Column(name = "depois_json")
    private String depoisJson;

    @Column(nullable = false, length = 100)
    private String usuario;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "data_hora")
    private LocalDateTime dataHora;
}