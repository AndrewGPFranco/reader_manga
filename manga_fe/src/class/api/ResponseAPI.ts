class ResponseAPI {
  private readonly _message: string;
  private readonly _statusCode: number;

  constructor(message: string, statusCode: number) {
    this._message = message;
    this._statusCode = statusCode;
  }

  get message(): string {
    return this._message
  }

  get statusCode(): number {
    return this._statusCode
  }
}

export default ResponseAPI;