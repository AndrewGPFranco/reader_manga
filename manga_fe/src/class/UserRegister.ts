export class UserRegister {
    private readonly firstName: string;
    private readonly username: string;
    private readonly fullName: string;
    private readonly dateBirth: Date;
    private readonly email: string;
    private readonly password: string;

    constructor(nome: string, apelido: string, nomeCompleto: string, dataNascimento: Date, email: string, senha: string) {
        this.firstName = nome;
        this.username = apelido;       
        this.fullName = nomeCompleto;
        this.dateBirth = dataNascimento;
        this.email = email;       
        this.password = senha;
    }
}