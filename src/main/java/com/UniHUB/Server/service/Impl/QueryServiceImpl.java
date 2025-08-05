package com.UniHUB.Server.service.Impl;

import com.UniHUB.Server.dao.StudentDAO;
import com.UniHUB.Server.dao.Impl.StudentDAOImpl;
import com.UniHUB.Server.dto.QueryDTO;
import com.UniHUB.Server.service.QueryService;
import java.util.List;

public class QueryServiceImpl implements QueryService {
    private StudentDAO studentDAO = new StudentDAOImpl();

    @Override
    public void submitQuery(QueryDTO query) {
        studentDAO.addQuery(query);
    }

    @Override
    public List<QueryDTO> getStudentQueries(int studentId) {
        return studentDAO.getQueriesByStudentId(studentId);
    }
}
