export interface UserInfo {
  id: number;
  username: string;
  fullName: string;
}

export interface AuthState {
  token: string | null;
  userInfo: UserInfo | null;
}
