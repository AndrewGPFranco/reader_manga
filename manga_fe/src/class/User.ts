export class User {

    private id?: string;
    private readonly email: string;
    private readonly password: string;
    private token?: string;

    constructor(email: string, password: string, token?: string, id?: string) {
        this.email = email;
        this.password = password;
        this.token = token;
        this.id = id;
    }

    getEmail(): string {
        return this.email;
    }

    getPassword(): string {
        return this.password;
    }

    getToken(): string | undefined {
        return this.token;
    }

    setToken(token: string): void {
        this.token = token;
    }

    setId(id: string): void {     
        this.id = id;
    }

    getId(): string | undefined {
        return this.id;
    }
    
}