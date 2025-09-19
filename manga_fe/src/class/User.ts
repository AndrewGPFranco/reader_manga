export class User {

    private id?: string;
    private readonly email: string;
    private token?: string;

    constructor(email: string, token?: string, id?: string) {
        this.email = email;
        this.token = token;
        this.id = id;
    }

    getEmail(): string {
        return this.email;
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