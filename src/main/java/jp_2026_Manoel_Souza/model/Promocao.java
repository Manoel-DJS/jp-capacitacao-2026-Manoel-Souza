package jp_2026_Manoel_Souza.model;

import jakarta.persistence.*;
import jp_2026_Manoel_Souza.model.enums.TipoPromocao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promocoes")
public class Promocao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoPromocao tipo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, name = "data_inicio")
    private LocalDateTime dataInicio;

    @Column(nullable = false, name = "data_fim")
    private LocalDateTime dataFim;

    @Column(nullable = false, name = "limite_uso")
    private Integer limiteUso;

    @Column(nullable = false, name = "quantidade_usada")
    private Integer quantidadeUsada = 0;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produtos produto;

    @Column(nullable = false)
    private Boolean ativo = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "data_criacao")
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(nullable = false, name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
}