package jp_2026_Manoel_Souza.model;

import jakarta.persistence.*;
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
@Table(name = "produtos")
public class Produtos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(name = "codigo_barras", length = 100)
    private String codigoBarras;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoCusto;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private Integer quantidadeEstoque = 0;

    @Column(nullable = false)
    private Integer estoqueMinimo = 0;

    @Column(nullable = false)
    private Boolean estoqueBaixo = false;

    @Column(nullable = false)
    private Boolean ativo = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "data_criacao")
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(nullable = false, name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(nullable = false, precision = 3, scale = 2, name = "media_avaliacoes")
    private BigDecimal mediaAvaliacoes = BigDecimal.ZERO;

    @Column(nullable = false, name = "quantidade_avaliacoes")
    private Integer quantidadeAvaliacoes = 0;
}