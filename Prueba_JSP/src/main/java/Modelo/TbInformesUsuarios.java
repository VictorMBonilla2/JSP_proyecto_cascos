package Modelo;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "tb_informepdf_usuario")
public class TbInformesUsuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona; // Relación con la entidad Persona

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Lob
    @Column(name = "file_data", nullable = false)
    private byte[] fileData;

    @Column(name = "upload_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "codigo_informe", unique = true, nullable = false, length = 50)
    private String codigoInforme;

    public TbInformesUsuarios() {
    }

    public TbInformesUsuarios(Long id, Persona persona, String fileName, byte[] fileData, Date uploadDate, String fileType, String codigoInforme) {
        this.id = id;
        this.persona = persona;
        this.fileName = fileName;
        this.fileData = fileData;
        this.uploadDate = uploadDate;
        this.fileType = fileType;
        this.codigoInforme = codigoInforme;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getCodigoInforme() {
        return codigoInforme;
    }

    public void setCodigoInforme(String codigoInforme) {
        this.codigoInforme = codigoInforme;
    }
}
