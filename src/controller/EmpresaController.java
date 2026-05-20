package controller;

import dao.EmpresaDAO;
import dao.EnderecoDAO;
import model.Conexao;
import model.Empresa;

public class EmpresaController {

    private EmpresaDAO dao;
    private Empresa empresa;

    public EmpresaController(Empresa empresa) {
        super();
        this.empresa = empresa;
    }

    /**
     * Salva a empresa e seu endereço no banco após verificar duplicatas de CNPJ e e-mail.
     * A tabela ENDERECOEMPRESA tem FK para EMPRESA, portanto insere a empresa primeiro,
     * obtém o ID gerado e usa-o para inserir em ENDERECOEMPRESA.
     *
     * @param complemento  complemento do endereço da empresa
     * @param numero       número do imóvel
     * @param idLogradouro ID do logradouro selecionado
     * @throws IllegalArgumentException se CNPJ ou e-mail já estiverem cadastrados
     * @throws Exception                se ocorrer erro de banco de dados
     */
    public void salvarComEndereco(String complemento, int numero, int idLogradouro) throws Exception {
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
            dao.inserirEnderecoEmpresa(complemento, numero, idLogradouro, idEmpresa);
        } finally {
            Conexao.desconectar();
        }
    }
}
