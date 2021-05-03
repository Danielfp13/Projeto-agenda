import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Contato } from './contato/contato';
import { PaginaContato } from './contato/paginaContato';

@Injectable({
  providedIn: 'root'
})
export class ContatoService {

private uri: string = environment.urlBase + "/contatos";

  constructor(private http: HttpClient) {

   }
   insert(contato: Contato): Observable<any>{
     return this.http.post<Contato>(`${this.uri}`,contato);
   }

   findAll(): Observable<Contato[]>{
    return this.http.get<any>(`${this.uri}`);
  }

  findPage(page: number, linePerPage: number, direction: string, orderBy: string): Observable<PaginaContato>{
    const params = new HttpParams()
      .set('page', page.toString())
      .set('linePerPage' , linePerPage.toString())
      .set( 'direction', direction)
      .set( 'orderBy', orderBy)
    
    return this.http.get<any>(`${this.uri}/page?${params.toString()}`);
  }

  favorite(contato: Contato): Observable<any>{
      return this.http.patch<any>(`${this.uri}/${contato.id}/favorito`,null);
  }

  upload(contato: Contato, formData: FormData): Observable<any>{
    return this.http.put(`${this.uri}/${contato.id}/foto`,formData, { responseType: 'blob' });
  } 
}
