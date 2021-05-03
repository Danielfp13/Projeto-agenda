package com.api.agenda.service;

import com.api.agenda.domain.Contato;
import com.api.agenda.repositories.ContatoRepository;
import com.api.agenda.service.exception.EmailDuplicadoException;
import com.api.agenda.service.exception.ObjectNotFoundException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class ContatoService {

    @Autowired
    private ContatoRepository repository;

    public Contato insert(Contato contato) {
        Contato aux = verificaEmail(contato.getEmail());
        if(aux != null){
            throw new EmailDuplicadoException("Já existe contato com esse e-mail.");
        }else
        return repository.save(contato);
    }

    public void updade(Contato contato, Integer id) {
        Contato aux = find(id);
        aux = verificaEmail(contato.getEmail());
        if(aux != null && !aux.getId().equals(id)){
            throw new EmailDuplicadoException("Já existe contato com esse e-mail.");
        }else{
            BeanUtils.copyProperties(contato,aux);
            aux.setId(id);
            repository.save(aux);
        }
    }

    public Contato verificaEmail(String email){
        return repository.findByEmail(email);
    }

    public void delete(Integer id) {
        find(id);
        try {
            repository.deleteById(id);
        }catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Não pode excluir contatos com associações.");
        }
    }

    public Contato find(Integer id) {
       return  repository.findById(id)
                .orElseThrow(()-> new ObjectNotFoundException("Não existe contatos com o id " + id + "."));

    }

    public List<Contato> findAll() {
        return repository.findAll();
    }

    public Page<Contato> findPage(Integer page, Integer linePerPage, String direction, String orderBy) {
        PageRequest pageRequest = PageRequest.of(page,linePerPage, Sort.Direction.valueOf(direction),orderBy);
        return repository.findAll(pageRequest);
    }

    public void favorite(Integer id) {
        Contato contato = find(id);
        boolean favorito = contato.getFavorito() == Boolean.TRUE;
        contato.setFavorito(!favorito);
        repository.save(contato);
    }

    public byte[] addPhoto(Integer id,Part arquivo) {

        Contato contato = find(id);
            try {
                InputStream is = arquivo.getInputStream();
                byte[] bytes = new byte[(int) arquivo.getSize()];
                IOUtils.readFully(is,bytes);
                contato.setFoto(bytes);
                repository.save(contato);
                is.close();
                return bytes;
            }catch (IOException e) {
                return null;
            }
    }
}
