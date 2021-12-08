export type GiftModel = {
  id: number;
  name: string;
  description: string;
  quantity: number;
  url: string | null;
};

export type GiftsModel = {
  gifts: GiftModel[];
};
