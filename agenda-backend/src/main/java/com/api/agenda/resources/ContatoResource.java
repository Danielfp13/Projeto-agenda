package com.api.agenda.resources;

import com.api.agenda.domain.Contato;
import com.api.agenda.service.ContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.Part;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/contatos")
@CrossOrigin("http://localhost:4200")
public class ContatoResource {

    @Autowired
    private ContatoService service;

    @PostMapping
    public ResponseEntity<Contato> insert(@Valid @RequestBody Contato contato)
    {
        contato = service.insert(contato);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(contato.getId()).toUri();
        return ResponseEntity.created(uri).body(contato);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updade(@Valid @RequestBody Contato contato, @PathVariable Integer id)
    {
        service.updade(contato,id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id)
    {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contato> find(@PathVariable Integer id)
    {
       Contato contato = service.find(id);
        return ResponseEntity.ok().body(contato);
    }

    @GetMapping()
    public ResponseEntity<List<Contato>> findAll()
    {
        List<Contato> contatos = service.findAll();
        return ResponseEntity.ok().body(contatos);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Contato>> findPge(
            @RequestParam(value = "page",defaultValue = "0") Integer page,
            @RequestParam(value = "linePerPage",defaultValue = "24") Integer linePerPage,
            @RequestParam(value = "direction",defaultValue = "ASC") String direction,
            @RequestParam(value = "orderBy",defaultValue = "nome") String orderBy
    )
    {
        Page<Contato> contatosPage = service.findPage(page,linePerPage,direction,orderBy);
        return ResponseEntity.ok().body(contatosPage);
    }

    @PatchMapping("/{id}/favorito")
    public ResponseEntity<Void> favorite(@PathVariable Integer id){
        service.favorite(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/foto")
    public ResponseEntity<byte[]> addPhoto(@PathVariable Integer id, @RequestParam ("foto") Part arquivo){
        byte[] file = service.addPhoto(id, arquivo);
        return ResponseEntity.noContent().build();
    }
}
