export interface CommentDto {
    id: number
    text: string
    author: string
    createdAt: string
}

export type Priority = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'

export interface TaskDto {
    id: number
    title: string
    description: string
    createdBy: string
    lastEditedBy: string
    todoDate: string
    priority: Priority
    comments: CommentDto[]
    done: boolean
}