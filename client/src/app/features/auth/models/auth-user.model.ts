export enum UserRole {
    ADMIN,

    PREMIUM,

    USER
}

export interface AuthUser {
    id: string;
    name: string;
    email: string;
    role: UserRole;
}
