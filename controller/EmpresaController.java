package controller;

import dao.EmpresaDAO;
import model.Conexao;
import model.Empresa;

public class EmpresaController {

    private EmpresaDAO dao;
    private Empresa empresa;

    public EmpresaController(Empresa empresa) {
        this.empresa = empresa;
    }

    /**
     * Valida unicidade de CNPJ/e-mail e insere a empresa (com endereço flat já preenchido no objeto).
     *
     * @throws IllegalArgumentException se CNPJ ou e-mail já estiverem cadastrados
     * @throws Exception                se ocorrer erro de banco de dados
     */
    public void salvar() throws Exception {
        Conexao.conectar();
        try {
            dao = new EmpresaDAO(Conexao.conexao);

            if (dao.existeCnpj(empresa.getCnpj())) {
                throw new IllegalArgumentException("CNPJ já cadastrado no sistema!");
            }
            if (dao.existeEmail(empresa.getEmailEmpresa())) {
                throw new IllegalArgumentException("E-mail já cadastrado no sistema!");
            }

            int idEmpresa = dao.inserir(empresa);
            if (idEmpresa <= 0) {
                throw new Exception("Falha ao registrar empresa no banco de dados.");
            }
            empresa.setIdEmpresa(idEmpresa);
        } finally {
            Conexao.desconectar();
        }
    }
}
