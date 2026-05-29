interface JwtPayload {
    sub?: string;
    name?: string;
    email?: string;
    type?: string;
    iat?: number;
    exp?: number;
    [key: string]: unknown;
}

function safeBase64UrlDecode(input: string): string {
    input = input.replaceAll('-', '+').replaceAll('_', '/');
    const pad = input.length % 4;
    if (pad === 2) input += '==';
    else if (pad === 3) input += '=';
    else if (pad !== 0) input += '===='.slice(pad);
    return atob(input);
}

export function decodeJwtPayload(token: string): JwtPayload | null {
    try {
        const parts = token.split('.');
        if (parts.length < 2) return null;
        const payloadJson = safeBase64UrlDecode(parts[1]);
        const parsed = JSON.parse(payloadJson) as JwtPayload | null;
        return parsed;
    } catch {
        return null;
    }
}
