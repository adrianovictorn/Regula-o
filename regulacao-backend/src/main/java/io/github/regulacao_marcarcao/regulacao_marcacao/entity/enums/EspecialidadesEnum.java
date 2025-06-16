package io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;



public enum EspecialidadesEnum {
    // --- ESPECIALIDADES MÉDICAS (Exemplos) ---
    ANGIOLOGISTA(ItemCategoria.ESPECIALIDADE_MEDICA, "Angiologista"),
    CARDIOLOGISTA(ItemCategoria.ESPECIALIDADE_MEDICA, "Cardiologista"),
    ENDOCRINOLOGISTA(ItemCategoria.ESPECIALIDADE_MEDICA, "Endocrinologista"),
    GASTROENTEROLOGISTA(ItemCategoria.ESPECIALIDADE_MEDICA, "Gastroenterologista"),
    MASTOLOGISTA(ItemCategoria.ESPECIALIDADE_MEDICA, "Mastologista"),
    DERMATOLOGISTA(ItemCategoria.ESPECIALIDADE_MEDICA, "Dermatologista"), // Mantido conforme DB, descrição corrigida
    NEFROLOGISTA(ItemCategoria.ESPECIALIDADE_MEDICA, "Nefrologista"),
    ORTOPEDISTA(ItemCategoria.ESPECIALIDADE_MEDICA, "Ortopedista"),
    OFTALMOLOGISTA(ItemCategoria.ESPECIALIDADE_MEDICA, "Oftalmologista"),
    NEUROLOGISTA(ItemCategoria.ESPECIALIDADE_MEDICA, "Neurologista"),
    NEUROPEDIATRIA(ItemCategoria.ESPECIALIDADE_MEDICA, "Neuropediatria"),
    PEDIATRA(ItemCategoria.ESPECIALIDADE_MEDICA, "PEDIATRA"),
    PEDIATRIA(ItemCategoria.ESPECIALIDADE_MEDICA, "PEDIATRIA"),
    OTORRINOLARINGOLOGISTA(ItemCategoria.ESPECIALIDADE_MEDICA, "Otorrinolaringologista"),
    UROLOGISTA(ItemCategoria.ESPECIALIDADE_MEDICA, "Urologista"),
    REUMATOLOGISTA(ItemCategoria.ESPECIALIDADE_MEDICA, "Reumatologista"),
    PNEUMOLOGISTA(ItemCategoria.ESPECIALIDADE_MEDICA, "Pneumologista"),
    HEMATOLOGISTA(ItemCategoria.ESPECIALIDADE_MEDICA, "Hematologista"), // Mantido conforme DB, descrição corrigida
    CONSULTA_OFTALMOLOGICA_REFRACAO(ItemCategoria.ESPECIALIDADE_MEDICA, "Consulta Oftalmológica (Refração)"),

    // --- EXAMES OU PROCEDIMENTOS (Exemplos) ---
    DOPPLER(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Doppler"),
    MAMOGRAFIA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Mamografia"),
    HOLTER(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Holter"),
    TESTE_ERGOMETRICO(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Teste Ergométrico"),
    MAPA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "M.A.P.A."),
    TOMOGRAFIA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Tomografia Computadorizada"),
    RESSONANCIA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Ressonância Magnética"),
    CINTILOGRAFIA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Cintilografia"),
    ELETRONEUROMIOGRAFIA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Eletroneuromiografia"),
    CATETERISMO(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Cateterismo"),
    HEMOGRAMA_COMPLETO(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Hemograma Completo"),
    GLICEMIA_JEJUM(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Glicemia de Jejum"),
    HEMOGLOBINA_GLICADA_HBA1C(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Hemoglobina Glicada (HbA1c)"),
    COLESTEROL_TOTAL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Colesterol Total"),
    HDL_COLESTEROL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "HDL Colesterol"),
    LDL_COLESTEROL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "LDL Colesterol"),
    VLDL_COLESTEROL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "VLDL Colesterol"),
    TRIGLICERIDEOS(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Triglicerídeos"),
    UREIA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Ureia"),
    CREATININA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Creatinina"),
    SODIO(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Sódio"),
    POTASSIO(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Potássio"),
    ACIDO_URICO(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Ácido Úrico"),
    SUMARIO_DE_URINA_EAS(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Sumário de Urina (EAS)"),
    UROCULTURA_COM_ANTIBIOGRAMA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Urocultura com Antibiograma"),
    PARASITOLOGICO_DE_FEZES(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Parasitológico de Fezes"),
    PESQUISA_SANGUE_OCULTO_FEZES(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Pesquisa de Sangue Oculto nas Fezes"),
    TESTE_RAPIDO_GRAVIDEZ_TIG(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Teste Rápido de Gravidez (TIG)"),
    TESTE_RAPIDO_HIV(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Teste Rápido para HIV"),
    SOROLOGIA_HIV(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Sorologia para HIV"),
    TESTE_RAPIDO_SIFILIS(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Teste Rápido para Sífilis"),
    VDRL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "VDRL"),
    TESTE_RAPIDO_HEPATITE_B(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Teste Rápido para Hepatite B"),
    HBSAG(ItemCategoria.EXAME_OU_PROCEDIMENTO, "HBsAg"),
    ANTI_HBS(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Anti-HBs"),
    TESTE_RAPIDO_HEPATITE_C(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Teste Rápido para Hepatite C"),
    ANTI_HCV(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Anti-HCV"),
    TSH_HORMONIO_TIREOESTIMULANTE(ItemCategoria.EXAME_OU_PROCEDIMENTO, "TSH"),
    T4_LIVRE(ItemCategoria.EXAME_OU_PROCEDIMENTO, "T4 Livre"),
    PSA_TOTAL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "PSA Total"),
    PSA_LIVRE(ItemCategoria.EXAME_OU_PROCEDIMENTO, "PSA Livre"),
    BACILOSCOPIA_DE_ESCARRO_BAAR(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Baciloscopia de Escarro (BAAR)"),
    CULTURA_DE_ESCARRO(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Cultura de Escarro"),
    ELETROCARDIOGRAMA_ECG(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Eletrocardiograma (ECG)"),
    COLPOSCOPIA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Colposcopia"),
    BIOPSIA_COLO_UTERINO(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Biópsia de Colo Uterino"),
    MAMOGRAFIA_BILATERAL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Mamografia Bilateral"),
    ULTRASSONOGRAFIA_PARTES_MOLES(ItemCategoria.EXAME_OU_PROCEDIMENTO, "USG Partes Mole"),
    ULTRASSONOGRAFIA_ABDOMINAL_TOTAL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "USG Abdominal Total"),
    ULTRASSONOGRAFIA_ABDOMEN_SUPERIOR(ItemCategoria.EXAME_OU_PROCEDIMENTO, "USG Abdômen Superior"),
    ULTRASSONOGRAFIA_PELVICA_VIA_ABDOMINAL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "USG Pélvica (Abdominal)"),
    ULTRASSONOGRAFIA_PELVICA_TRANSVAGINAL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "USG Pélvica (Transvaginal)"),
    ULTRASSONOGRAFIA_OBSTETRICA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "USG Obstétrica"),
    ULTRASSONOGRAFIA_VIAS_URINARIAS(ItemCategoria.EXAME_OU_PROCEDIMENTO, "USG Vias Urinárias"),
    ULTRASSONOGRAFIA_PROSTATA_VIA_ABDOMINAL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "USG Próstata (Abdominal)"),
    ULTRASSONOGRAFIA_TIREOIDE(ItemCategoria.EXAME_OU_PROCEDIMENTO, "USG Tireoide"),
    ULTRASSONOGRAFIA_ARTICULAR(ItemCategoria.EXAME_OU_PROCEDIMENTO, "USG Articular"),
    ULTRASSONOGRAFIA_DOPPLER_ARTERIAL_MEMBRO_INFERIOR_UNILATERAL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "USG Doppler Arterial MI Unilateral"),
    ULTRASSONOGRAFIA_DOPPLER_ARTERIAL_MEMBRO_INFERIOR_BILATERAL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "USG Doppler Arterial MI Bilateral"),
    ULTRASSONOGRAFIA_DOPPLER_VENOSO_MEMBRO_INFERIOR_UNILATERAL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "USG Doppler Venoso MI Unilateral"),
    ULTRASSONOGRAFIA_DOPPLER_VENOSO_MEMBRO_INFERIOR_BILATERAL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "USG Doppler Venoso MI Bilateral"),
    ULTRASSONOGRAFIA_DOPPLER_CAROTIDAS_E_VERTEBRAIS(ItemCategoria.EXAME_OU_PROCEDIMENTO, "USG Doppler Carótidas e Vertebrais"),
    RAIO_X_TORAX_PA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Raio-X Tórax PA"),
    RAIO_X_TORAX_PA_PERFIL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Raio-X Tórax PA/Perfil"),
    RAIO_X_SEIOS_DA_FACE(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Raio-X Seios da Face"),
    RAIO_X_COLUNA_CERVICAL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Raio-X Coluna Cervical"),
    RAIO_X_COLUNA_DORSAL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Raio-X Coluna Dorsal"),
    RAIO_X_COLUNA_LOMBO_SACRA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Raio-X Coluna Lombo-Sacra"),
    RAIO_X_ABDOMEN_SIMPLES(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Raio-X Abdômen Simples"),
    RAIO_X_ABDOMEN_AGUDO(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Raio-X Abdômen Agudo"),
    RAIO_X_ARTICULACAO_COXO_FEMURAL_BACIA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Raio-X Bacia"),
    RAIO_X_JOELHO(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Raio-X Joelho"),
    RAIO_X_MAO_OU_QUIRODACTILOS(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Raio-X Mão/Quirodáctilos"),
    RAIO_X_PE_OU_PODODACTILOS(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Raio-X Pé/Pododáctilos"),
    TESTE_ERGOMETRICO_CONVENCIONAL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Teste Ergométrico Convencional"),
    HOLTER_24_HORAS_3_CANAIS(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Holter 24 Horas (3 Canais)"),
    MAPA_24_HORAS_MONITORIZACAO_AMBULATORIAL_PRESSAO_ARTERIAL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "MAPA 24 Horas"),
    ENDOSCOPIA_DIGESTIVA_ALTA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Endoscopia Digestiva Alta"),
    ENDOSCOPIA_DIGESTIVA_ALTA_COM_BIOPSIA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Endoscopia Digestiva Alta com Biópsia"),
    COLONOSCOPIA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Colonoscopia"),
    COLONOSCOPIA_COM_BIOPSIA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Colonoscopia com Biópsia"),
    AUDIOMETRIA_TONAL_LIMIAR_COM_TESTES_DE_DISCRIMINACAO(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Audiometria Tonal e Vocal"),
    IMPEDANCIOMETRIA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Impedanciometria"),
    FUNDOSCOPIA_BINOCULAR_INDIRETA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Fundoscopia Binocular Indireta"),
    TONOMETRIA_APLANACAO(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Tonometria de Aplanação"),
    DENSITOMETRIA_OSSEA_COLUNA_E_FEMUR(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Densitometria Óssea"),
    ECOCARDIOGRAMA_TRANSTORACICO_MODO_M_BIDIMENSIONAL_DOPPLER(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Ecocardiograma Transtorácico com Doppler"),
    ESPIROMETRIA_PROVA_VENTILATORIA_COMPLETA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Espirometria Completa"),
    ELETROENCEFALOGRAMA_CONVENCIONAL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Eletroencefalograma Convencional"),
    TOMOGRAFIA_COMPUTADORIZADA_CRANIO(ItemCategoria.EXAME_OU_PROCEDIMENTO, "TC Crânio"),
    TOMOGRAFIA_COMPUTADORIZADA_TORAX(ItemCategoria.EXAME_OU_PROCEDIMENTO, "TC Tórax"),
    TOMOGRAFIA_COMPUTADORIZADA_ABDOMEN_TOTAL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "TC Abdômen Total"),
    RESSONANCIA_MAGNETICA_CRANIO_ENCEFALO(ItemCategoria.EXAME_OU_PROCEDIMENTO, "RM Crânio/Encéfalo"),
    RESSONANCIA_MAGNETICA_COLUNA_CERVICAL(ItemCategoria.EXAME_OU_PROCEDIMENTO, "RM Coluna Cervical"),
    RESSONANCIA_MAGNETICA_COLUNA_LOMBAR(ItemCategoria.EXAME_OU_PROCEDIMENTO, "RM Coluna Lombar"),
    RESSONANCIA_MAGNETICA_JOELHO(ItemCategoria.EXAME_OU_PROCEDIMENTO, "RM Joelho"),
    CINTILOGRAFIA_MIOCARDIO_PERFUSAO_REPOUSO_ESTRESSE(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Cintilografia Miocárdio (Repouso/Estresse)"),
    CINTILOGRAFIA_TIREOIDE_CAPTACAO(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Cintilografia Tireoide com Captação"),
    CINTILOGRAFIA_OSSEA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Cintilografia Óssea"),
    ELETRONEUROMIOGRAFIA_MEMBROS_SUPERIORES(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Eletroneuromiografia MMSS"),
    ELETRONEUROMIOGRAFIA_MEMBROS_INFERIORES(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Eletroneuromiografia MMII"),
    CATETERISMO_CARDIACO_ESQUERDO_DIAGNOSTICO(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Cateterismo Cardíaco Diagnóstico"),
    AVALIACAO_URODINAMICA_COMPLETA(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Avaliação Urodinâmica Completa"),
    EXERESE_PEQUENAS_LESOES_PELE_ANATOMOPATOLOGICO(ItemCategoria.EXAME_OU_PROCEDIMENTO, "Exerese Pequenas Lesões de Pele + AP");

    private final ItemCategoria categoria;
    private final String descricao;

    EspecialidadesEnum(ItemCategoria categoria, String descricao) {
        this.categoria = categoria;
        this.descricao = descricao;
    }

    public ItemCategoria getCategoria() {
        return categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public static List<EspecialidadesEnum> getItensPorCategoria(ItemCategoria categoria) {
        return Arrays.stream(values())
                .filter(item -> item.getCategoria() == categoria)
                .collect(Collectors.toList());
    }
}