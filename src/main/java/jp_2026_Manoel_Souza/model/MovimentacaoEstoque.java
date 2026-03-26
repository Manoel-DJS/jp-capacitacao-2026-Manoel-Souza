package jp_2026_Manoel_Souza.model;

import jakarta.persistence.*;
import jp_2026_Manoel_Souza.model.enums.TipoMovimentacaoEstoque;
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
@Table(name = "movimentacoes_estoque")
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produtos produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimentacaoEstoque tipo;

    @Column(nullable = false, length = 150)
    private String motivo;

    @Column(name = "referencia_id")
    private Long referenciaId;

    @Column(length = 100)
    private String criadoPor;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "data_criacao")
    private LocalDateTime dataCriacao;
}