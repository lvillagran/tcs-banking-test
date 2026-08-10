package com.banking.operaciones.model;

import com.banking.core.infraestrutura.model.BaseEntidadAuditoria;
import com.banking.operaciones.model.enums.TipoMovimiento;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "TAB_MOVIMIENTOS", schema = "operaciones")
public class BanMovimientos extends BaseEntidadAuditoria implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Date fechaMovimiento;
    private  String movimiento;
    private TipoMovimiento tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
    private  String numeroCuenta;
    private  Boolean estado;
    private BanCuenta cuenta;


    @Id
    @GeneratedValue(generator = "secTabMovimiento", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "secTabMovimiento", allocationSize = 1, initialValue = 1, sequenceName = "SEC_MOVIMIENTO")
    @Column(name = "ID_MOVIMIENTO")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "FECHA_MOVIMIENTO")
    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    @Column(name = "MOVIMIENTO")
    public String getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_MOVIMIENTO", nullable = false, length = 3)
    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }


    @Column(name = "VALOR_MOVIMIENTO")
    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Column(name = "SALDO")
    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }


    @ManyToOne
    @JoinColumn(name = "ID_CUENTA")
    public BanCuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(BanCuenta cuenta) {
        this.cuenta = cuenta;
    }

    @Column(name = "numero_cuenta")
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    @Column(name = "ESTADO")
    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
