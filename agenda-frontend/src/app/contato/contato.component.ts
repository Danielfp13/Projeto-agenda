import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ContatoService } from '../contato.service';
import { Contato } from './contato';
import { MatDialog } from '@angular/material/dialog'
import { ContatoDetalheComponent } from '../contato-detalhe/contato-detalhe.component';
import { PageEvent } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';


@Component({
  selector: 'app-contato',
  templateUrl: './contato.component.html',
  styleUrls: ['./contato.component.css']
})
export class ContatoComponent implements OnInit {

  formulario: FormGroup;
  contatos: Contato[] = [];

  page: number = 0;
  linePerPage: number = 8;
  direction: string = 'ASC';
  orderBy: string = 'id';
totalElementos: number = 0;
totalPage: number = 0;
pageSizeOptions: number[] = [8]

  colunas = ['foto', 'id', 'nome', 'email', 'favorito'];

  constructor(
    private service: ContatoService,
    private fb: FormBuilder,
    private dialog: MatDialog,
    private snackBar: MatSnackBar


  ) { }


  ngOnInit(): void {
    this.montarFormulario();
    this.findPage(this.page, this.linePerPage, this.direction, this.orderBy );
  }


  montarFormulario() {
    this.formulario = this.fb.group({
      nome: ['', Validators.required],
      email: ['', [Validators.email, Validators.required]]
    })
  }

  favoritar(contato: Contato) {

    this.service.favorite(contato).subscribe(
      response => {
        contato.favorito = !contato.favorito;
      }
    );


  }

  findPage( page: number , linePerPage: number,direction :string, orderBy: string) {
    this.service.findPage(page,linePerPage, direction, orderBy).subscribe(
      response => {
        this.contatos = response.content
        this.totalElementos = response.totalElements
        this.totalPage = response.totalPages
        this.page = response.number
      }, responseError => {

      }
    );
  }

  paginar(event: PageEvent){
    this.page = event.pageIndex
    this.findPage(this.page, this.linePerPage, this.direction, this.orderBy);
  }

  findAll() {
    this.service.findAll().subscribe(
      response => {
        this.contatos = response
      }, responseError => {

      }
    );
  }

  onSubmit() {
    const formValues = this.formulario.value;
    const contato: Contato = new Contato(formValues.nome, formValues.email);


    //Object.keys(this.formulario.controls).forEach( campo=>{
     // console.log(campo.);

    //})


    this.service.insert(contato).subscribe(
      response => {
        this.findPage(this.page, this.linePerPage, this.direction, this.orderBy);
        this.snackBar.open('Contato adicionado com sucesso.','Sucesso',{
          duration: 6000
        });
        this.formulario.reset();
        this.formulario.controls.nome.markAsUntouched
        this.formulario.controls.email.markAsUntouched
        console.log(response)
      }, responseError => {
        console.log(responseError)
      }
    );
    
  }

  uploadFoto(event: any, contato: Contato) {
    const files = event.target.files;
    if (files) {
      const foto = files[0];
      const formData: FormData = new FormData();
      formData.append("foto", foto);
      this.service.upload(contato, formData).subscribe(
        response => {
          this.findAll();
        }
      );
    }
  }

  visualizarContato(contato: Contato){
    this.dialog.open( ContatoDetalheComponent,{
      width: '400px',
      height: '450px',
      data: contato
    })
  }

}



