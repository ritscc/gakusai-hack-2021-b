export type AlertModel = {
  statusCode: number;
  message: string;
  level: 'SUCCESS' | 'INFO' | 'WARN' | 'ERROR';
};
