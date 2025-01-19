export class User {

    private email: string;
    private password: string;
    private token?: string;

    constructor(email: string, password: string, token?: string) {
        this.email = email;
        this.password = password;
        this.token = token;
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
    
}