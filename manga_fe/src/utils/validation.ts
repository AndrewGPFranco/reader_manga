import type iLogin from "@/interface/iLogin";
import type iRegisterUser from "@/interface/iRegisterUser";
import type MangaData from "@/interface/Manga";

export function validationFields(data: MangaData): string | boolean{
    if(data.title === "") {
        return "Field title is required";
    }

    if(data.description === "") {
        return "Field description is required";
    }

    if(data.size === 0) {
        return "Field size is required";
    }

    if(data.creationDate === null) {
        return "Field creationDate is required";
    }

    if(data.endDate === null) {
        return "Field closingDate is required";
    }

    if(data.status === null) {
        return "Field status is required";
    }

    if(data.author === "") {
        return "Field author is required";
    }

    if(data.gender === "") {
        return "Field gender is required";
    }

    if(data.image === "") {
        return "Field image is required";
    }

    return true;
}

export function validationFieldsLogin(data: iLogin): string | boolean {
    if(data.email === null || data.email === "") {
        return "Email é obrigatório!"
    }

    if(data.password === null || data.password === "") {
        return "Senha é obrigatório!"
    }

    return true;
}

export function validationFieldsRegister(data: iRegisterUser): string | boolean {
    if (!data.nome?.trim()) {
        return "Nome é obrigatório!";
    }

    if (!data.apelido?.trim()) {
        return "Apelido é obrigatório!";
    }

    if (!data.nomeCompleto?.trim()) {
        return "Nome completo é obrigatório!";
    }

    if (data.nomeCompleto.split(' ').length < 2) {
        return "Por favor, insira seu nome completo!";
    }

    if (!data.dataNascimento?.trim()) {
        return "Data de nascimento é obrigatória!";
    }

    const dateRegex = /^(\d{2})\/(\d{2})\/(\d{4})$/;
    if (!dateRegex.test(data.dataNascimento)) {
        return "Data de nascimento inválida! Use o formato DD/MM/AAAA";
    }

    const [dia, mes, ano] = data.dataNascimento.split('/');
    const dataNasc = new Date(Number(ano), Number(mes) - 1, Number(dia));
    const hoje = new Date();
    
    if (!data.email?.trim()) {
        return "Email é obrigatório!";
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(data.email)) {
        return "Email inválido!";
    }

    if (!data.senha?.trim()) {
        return "Senha é obrigatória!";
    }

    if (data.senha.length < 8) {
        return "Senha deve ter no mínimo 8 caracteres!";
    }

    const senhaRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]/;
    if (!senhaRegex.test(data.senha)) {
        return "Senha deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial!";
    }

    if (!data.confirmarSenha?.trim()) {
        return "Confirmação de senha é obrigatória!";
    }

    if (data.senha !== data.confirmarSenha) {
        return "As senhas não coincidem!";
    }

    return true;
}