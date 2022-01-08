package com.pa2.genesrn.repository;

import com.pa2.genesrn.enums.EnumGenero;
import com.pa2.genesrn.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ProdutoCustomRepositoryImpl implements ProdutoCustomRepository {
    @Autowired
    private DataSource dt;
    @Override
    public List<Produto> listarProdutoPorUsuario(Integer idUsuario) {

        MapSqlParameterSource parameters = new MapSqlParameterSource();

        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT * FROM produto p JOIN usuario u ON (u.id = p.usuario_id) ");

        if(Objects.nonNull(idUsuario)){
            String strFiltroUsuario = "AND u.id = :idUsuario ";
            queryBuilder.append(strFiltroUsuario);

            parameters.addValue("idUsuario", idUsuario);
        }

        List<Produto> produtos = new NamedParameterJdbcTemplate(dt).query(queryBuilder.toString(), parameters,
                new RowMapper<Produto>() {
                    @Override
                    public Produto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Produto produto = new Produto();
                        produto.setNome(rs.getString("nome"));
                        produto.setDescricao(rs.getString("descricao"));
                        produto.setValor(rs.getDouble("valor"));
                        produto.setFotoReprodutor(rs.getString("foto_reprodutor"));
                        produto.setQuantidade(rs.getInt("quantidade"));
                        produto.setGenero(EnumGenero.valueOf(rs.getString("genero")));
                        return produto;

                    }
                });
        return produtos;
    }
}
