package br.com.alevh.sistema_adocao_pets.util;

public enum UsuarioRole {
    ADMIN("admin"),
    USER("usuario");

    private String role;

    UsuarioRole(String role){
        this.role = role;
    }
    public String getRole(){
        return role;
    }
}
